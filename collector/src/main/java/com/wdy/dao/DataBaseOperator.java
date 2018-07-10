package com.wdy.dao;

import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.google.gson.Gson;
import com.wdy.dao.DBHelper;

public abstract class DataBaseOperator<T> {
	private final static Logger logger = LoggerFactory.getLogger(DataBaseOperator.class);
	protected MongoCollection<Document> collection;
	private Type clazz;	// Real type of parameter T 

	public DataBaseOperator(String collectionName) {
		collection = DBHelper.getCollection(collectionName);

		// Super class's parameters type
		ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
		this.clazz = pt.getActualTypeArguments()[0];
		logger.debug("Param Class T: " + clazz.toString());
	}

	// Insert
	public void insert(Object data) {
		Gson gson = new Gson();
		Document doc = Document.parse(gson.toJson(data));
		collection.insertOne(doc);
	}

	public void insert(List<Object> datas) {
		Gson gson = new Gson();
		List<Document> docs = new ArrayList<Document>();
		for (Object obj : datas) {
			Document doc = Document.parse(gson.toJson(obj));
			docs.add(doc);
		}
		collection.insertMany(docs);
	}

	// Query
	public List<T> query() {
		return query(null);
	}

	public List<T> query(Bson filter) {
		List<T> datas = new ArrayList<T>();
		Iterable<Document> docs = (filter == null) ? collection.find() : collection.find(filter);
		Gson gson = new Gson();
		for (Document doc : docs) {
			doc.toJson();
			T data = gson.fromJson(doc.toJson(), clazz);
			datas.add(data);
		}
		return datas;
	}

	// Delete
	public boolean delete(Bson query) {
		DeleteResult result = collection.deleteMany(query);
		logger.debug("Result: " + result.wasAcknowledged() + ", deleted lines: " + result.getDeletedCount());
		return result.wasAcknowledged();
	}

	// Update
	public boolean update(Bson query, Bson updates) {
		UpdateResult result = collection.updateMany(query, updates);
		logger.debug("Result: " + result.wasAcknowledged() + ", matched lines: " + result.getMatchedCount()
				+ ", modified lines: " + result.getModifiedCount());
		return result.wasAcknowledged();
	}
}
