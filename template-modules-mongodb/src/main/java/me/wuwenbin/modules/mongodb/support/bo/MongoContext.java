package me.wuwenbin.modules.mongodb.support.bo;

/**
 * Created by wuwenbin on 2017/5/4.
 */
public class MongoContext {
    private String key;
    private String database;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public static MongoContext create(String key, String database) {
        MongoContext mongoContext = new MongoContext();
        mongoContext.setKey(key);
        mongoContext.setDatabase(database);
        return mongoContext;
    }

}
