package com.wdy.dao;

import java.util.ArrayList;
import java.util.function.Consumer;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.ListCollectionsIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.wdy.common.Config;

public class DBHelper {
	private static final Logger logger = LoggerFactory.getLogger(DBHelper.class);

	private static final String DB_SERVER = "db.server";
	private static final String DB_PORT = "db.port";
	private static final String DB_NAME = "db.name";
	private static final int DB_DEFAULT_PORT = 27017;

	private static MongoClient mongoClient;

	public static MongoClient dbClient() {
		if (mongoClient == null) {
			String dbServer = Config.instance().get(DB_SERVER);
			int dbPort = 0;
			try {
				dbPort = Integer.parseInt(Config.instance().get(DB_PORT));
			} catch (NumberFormatException e) {
				dbPort = DB_DEFAULT_PORT;
			}
			mongoClient = new MongoClient(dbServer, dbPort);
		}
		return mongoClient;
	}

	public static MongoDatabase getDB() {
		String dbName = Config.instance().get(DB_NAME);
		MongoDatabase myDB = dbClient().getDatabase(dbName);
		if (myDB == null) {
			logger.error("%s not found. Please ensure it's already created.", dbName);
		}
		return myDB;
	}

	public static MongoCollection<Document> getCollection(String collection) {
		MongoDatabase myDB = getDB();
		if (!myDB.listCollectionNames().into(new ArrayList<String>()).contains(collection)) {
			logger.debug("Collection " + collection + " does not exist, create one.");
			myDB.createCollection(collection);
		}
		return myDB.getCollection(collection);
	}

	public static void closeDB() {
		mongoClient.close();
		mongoClient = null;
	}

	public void demo() {
		String DB_COLLECTION = "people";
		MongoClient mongoClient = new MongoClient(DB_SERVER, 27017);

		logger.debug("Database list:");
		mongoClient.listDatabaseNames().forEach((Consumer<String>) System.out::println);

		/*
		 * MongoIterable<String> dbs = mongoClient.listDatabaseNames();
		 * MongoDatabase myDb = null; for (String db : dbs) { logger.debug(
		 * "DB: " + db); if (DB_NAME.equals(db)) { myDb =
		 * mongoClient.getDatabase(db); } }
		 */

		// connect to DB
		MongoDatabase myDb = mongoClient.getDatabase(DB_NAME);
		// mongoClient.listDatabaseNames().
		if (myDb == null) {
			logger.error("%s does not exist.", DB_NAME);
			mongoClient.close();
			return;
		}

		// List all collections
		ListCollectionsIterable<Document> collections = myDb.listCollections();
		logger.debug("Collections list:");
		collections.forEach((Block<Document>) item -> System.out.printf(item.toJson()));

		for (Document collection : collections) {
			logger.debug("collection:\n" + collection.toJson());

		}

		MongoCollection<Document> collection = null;

		// Create collection
		if (!myDb.listCollectionNames().into(new ArrayList<String>()).contains(DB_COLLECTION)) {
			logger.debug("Collection " + DB_COLLECTION + " does not exist, create one.");
			myDb.createCollection(DB_COLLECTION);
		}

		collection = myDb.getCollection(DB_COLLECTION);
		logger.debug("Get collection: " + collection.toString());

		myDb.listCollections().forEach((Block<Document>) item -> System.out.printf(item.toJson()));
		;

		// Insert document
		Document document = new Document();
		document.append("_id", "bl").append("name", "Bai Li").append("age", 58).append("sex", "male").append("ress",
				"Queen Street");

		collection.insertOne(document);
		logger.debug("collection.count: " + collection.count());
		mongoClient.close();
	}
}
