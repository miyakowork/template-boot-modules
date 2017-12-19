package me.wuwenbin.modules.mongodb.support.core;

import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import me.wuwenbin.modules.mongodb.support.pojo.MongoAuth;
import org.springframework.data.mongodb.core.convert.MongoConverter;

import java.util.List;
import java.util.Vector;

/**
 * Created by wuwenbin on 2017/4/22.
 */
public class StandardMongoDataSource implements MongoDataSource {

    /**
     * 集群配置（mongos）配了才有多条记录添加到此list中，否则不启用集群配置（仅仅是多台mongo）的话，添加一条记录到这个list中即可
     * 如果仅仅是为了多个mongo的话（类似多数据源），则需要多个MongoDataSource来配合，此处仅仅一条记录
     */
    private List<ServerAddress> seeds;

    /**
     * 当前mongo数据库中所有文档的认证用户及其对应的可操作的数据库
     */
    private List<MongoAuth> authenticates;

    /**
     * 当前dataSource默认操作的数据库，可以使用此字段当做唯一索引
     */
    private String defaultDatabase;

    /**
     * 一些参数配置,无配置则是默认配置
     */
    private MongoClientOptions mongoClientOptions = MongoClientOptions.builder().build();

    /**
     * 处理mongodb自动加的_class字段，如果想自定义处理，则请实现此接口，默认null（即_class将会默认插入到数据库中）
     */
    private MongoConverter mongoConverter = null;

    @Override
    public List<ServerAddress> getSeeds() {
        return seeds;
    }

    public void setSeeds(List<ServerAddress> seeds) {
        this.seeds = seeds;
    }

    public List<MongoAuth> getAuthenticates() {
        return authenticates;
    }

    public void setAuthenticates(List<MongoAuth> authenticates) {
        this.authenticates = authenticates;
    }

    @Override
    public String getDefaultDatabase() {
        return defaultDatabase;
    }

    public void setDefaultDatabase(String defaultDatabase) {
        this.defaultDatabase = defaultDatabase;
    }

    @Override
    public MongoClientOptions getMongoClientOptions() {
        return mongoClientOptions;
    }

    public void setMongoClientOptions(MongoClientOptions mongoClientOptions) {
        this.mongoClientOptions = mongoClientOptions;
    }

    @Override
    public MongoConverter getMongoConverter() {
        return mongoConverter;
    }

    public void setMongoConverter(MongoConverter mongoConverter) {
        this.mongoConverter = mongoConverter;
    }

    @Override
    public List<MongoCredential> getMongoCredentials() {
        List<MongoCredential> credentials = new Vector<>();
        for (MongoAuth mongoAuth : getAuthenticates()) {
            MongoCredential mongoCredential = MongoCredential.createCredential(mongoAuth.getUsername(), mongoAuth.getAuthDatabase(), mongoAuth.getPassword().toCharArray());
            credentials.add(mongoCredential);
        }
        return credentials;
    }
}
