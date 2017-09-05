package me.wuwenbin.modules.mongodb.support.core;

import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.data.mongodb.core.convert.MongoConverter;

import java.util.List;

/**
 * 组成mongo数据源的四大属性，实现类必须实现方法
 * Created by wuwenbin on 2017/4/23.
 */
public interface MongoDataSource {

    List<ServerAddress> getSeeds();

    List<MongoCredential> getMongoCredentials();

    String getDefaultDatabase();

    MongoClientOptions getMongoClientOptions();

    MongoConverter getMongoConverter();

}
