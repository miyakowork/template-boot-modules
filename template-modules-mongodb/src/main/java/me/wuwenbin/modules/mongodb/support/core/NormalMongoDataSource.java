package me.wuwenbin.modules.mongodb.support.core;

import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import me.wuwenbin.modules.mongodb.support.pojo.MongoAuth;
import org.springframework.data.mongodb.core.convert.MongoConverter;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Vector;

/**
 * 最普通的也是属性最少最简洁最快捷的配置方式类
 * Created by wuwenbin on 2017/4/23.
 */
public class NormalMongoDataSource implements MongoDataSource {

    private String host = "127.0.0.1";
    private int port = 27017;
    private List<MongoAuth> mongoAuthList;
    private String defaultDatabase;


    @Override
    public List<ServerAddress> getSeeds() {
        List<ServerAddress> serverAddresses = new Vector<>();
        try {
            ServerAddress serverAddress = new ServerAddress(getHost(), getPort());
            serverAddresses.add(serverAddress);
            return serverAddresses;
        } catch (UnknownHostException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public List<MongoCredential> getMongoCredentials() {
        List<MongoCredential> mongoCredentials = new Vector<>();
        for (MongoAuth mongoAuth : getMongoAuthList()) {
            MongoCredential mongoCredential = MongoCredential.createCredential(mongoAuth.getUsername(), mongoAuth.getAuthDatabase(), mongoAuth.getPassword().toCharArray());
            mongoCredentials.add(mongoCredential);
        }
        return mongoCredentials;
    }

    @Override
    public String getDefaultDatabase() {
        return defaultDatabase;
    }

    /**
     * 你没看错，这是返回默认的配置
     *
     * @return
     */
    @Override
    public MongoClientOptions getMongoClientOptions() {
        return MongoClientOptions.builder().build();
    }

    /**
     * 对！你还是没看错，就是返回null
     *
     * @return
     */
    @Override
    public MongoConverter getMongoConverter() {
        return null;
    }


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public List<MongoAuth> getMongoAuthList() {
        return mongoAuthList;
    }

    public void setMongoAuthList(List<MongoAuth> mongoAuthList) {
        this.mongoAuthList = mongoAuthList;
    }

    public void setDefaultDatabase(String defaultDatabase) {
        this.defaultDatabase = defaultDatabase;
    }
}
