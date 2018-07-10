package com.wdy.common;

public final class Constants {
    private Constants() {
    }

    public static final String CONFIG_FILE_NAME = "config.properties";

    // Config items
    public static final String CFG_CRAWL_STORAGE_FOLDER = "job.crawl.storage.folder";
    public static final String CFG_CRAWLERS_NUMBERS = "job.crawlers.number";
    public static final String CFG_OUTPUT_FILE = "job.output.file";
    public static final String CFG_MAX_PAGE_NUM = "job.max.page.number";
    public static final String CFG_SEEDURL_FILE = "job.seedurl";
    public static final String CFG_CRAWLER = "crawler.class.name";
}
