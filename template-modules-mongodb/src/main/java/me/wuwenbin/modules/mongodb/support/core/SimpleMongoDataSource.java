package me.wuwenbin.modules.mongodb.support.core;

import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import me.wuwenbin.modules.mongodb.constant.MongoConst;
import me.wuwenbin.modules.mongodb.support.exception.PortNotMatchHostException;
import org.springframework.data.mongodb.core.convert.MongoConverter;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Vector;

/**
 * 简单类型的属性配置，不必须配置List、Map等复杂属性
 * Created by wuwenbin on 2017/4/23.
 */
public class SimpleMongoDataSource implements MongoDataSource, MongoConst {

    /**
     * 所有主机的ip，集群才配置多个。没有配置集群则配置一个即可。集群配置多个之间用分号{@code ;}隔开
     */
    private String hosts = "127.0.0.1";

    /**
     * 如果端口全部一致，则配置一个即可，否则需配置与host同样数量的port，且顺序一一对应
     */
    private String ports = "27017";


    /**
     * userNames/passwords/authDatabases组成MongoAuth的List集合,数量必须一致，且一一对应
     */
    private String userNames;
    private String passwords;
    private String authDatabases;

    /**
     * 默认数据库
     */
    private String defaultDatabase;

    /**
     * 以下的所有参数将是组成{@link com.mongodb.MongoClientOptions}的属性
     */

    private int minConnectionsPerHost = 10;//每个主机最小连接数，默认10
    private int connectionsPerHost = 100;//每个主机最大的连接数，默认100
    private int threadsAllowedToBlockForConnectionMultiplier = 5;//此参数乘以connectionsPerHost等于一个线程变为可用的最大阻塞数，超过此乘积数之后的所有线程将及时获取一个异常,默认5
    private int maxWaitTime = 1000 * 60 * 2;//一个线程等待链接可用的最大等待毫秒数，0表示不等待，负数表示等待时间不确定，默认120000
    private int maxConnectionLifeTime = 100000;//设置连接池最长生存时间
    private int connectTimeout = 10000;//链接超时的毫秒数,0表示不超时,此参数只用在新建一个新链接时，默认10000
    private int socketTimeout = 0;//此参数表示socket I/O读写超时时间,推荐为不超时，即 0 Socket.setSoTimeout(int)
    private boolean socketKeepAlive = true;//该标志用于控制socket保持活动的功能，通过防火墙保持连接活着，默认true

    /**
     * 处理mongodb自动加的_class字段，如果想自定义处理，则请事先此接口，默认null（即_class将会默认插入到数据库中）
     */
    private MongoConverter mongoConverter;

    @Override
    public List<ServerAddress> getSeeds() {
        String[] hosts = getHosts().split(SPLIT);
        String[] ports = getPorts().split(SPLIT);
        if (ports.length > 1 && (hosts.length != ports.length))
            throw new PortNotMatchHostException("端口号与主机IP地址不匹配");
        else if ((hosts.length == ports.length) && ports.length > 1) {
            List<ServerAddress> seeds = new Vector<>();
            for (int i = 0; i < hosts.length; i++) {
                String host = hosts[i];
                int port = Integer.parseInt(ports[i]);
                try {
                    ServerAddress address = new ServerAddress(host, port);
                    seeds.add(address);
                } catch (UnknownHostException e) {
                    throw new RuntimeException("不正确的Host配置", e);
                }
            }
            return seeds;
        } else if (ports.length == 1) {
            List<ServerAddress> seeds = new Vector<>();
            int port = Integer.parseInt(ports[0]);
            for (String host : hosts) {
                try {
                    ServerAddress address = new ServerAddress(host, port);
                    seeds.add(address);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
            return seeds;
        } else throw new RuntimeException("获取serverAddress地址异常");
    }

    @Override
    public List<MongoCredential> getMongoCredentials() {
        String[] userNames = getUserNames().split(SPLIT);
        String[] passwords = getPasswords().split(SPLIT);
        String[] authDatabases = getAuthDatabases().split(SPLIT);
        if (userNames.length == passwords.length && passwords.length == authDatabases.length && userNames.length == authDatabases.length) {
            List<MongoCredential> credentials = new Vector<>();
            for (int i = 0; i < userNames.length; i++) {
                String userName = userNames[i];
                String password = passwords[i];
                String authDatabase = authDatabases[i];
                MongoCredential mongoCredential = MongoCredential.createCredential(userName, authDatabase, password.toCharArray());
                credentials.add(mongoCredential);
            }
            return credentials;
        } else throw new RuntimeException("MongoCredential参数获取异常");
    }

    @Override
    public MongoClientOptions getMongoClientOptions() {
        return MongoClientOptions.builder()
                .minConnectionsPerHost(getMinConnectionsPerHost())
                .connectionsPerHost(getConnectionsPerHost())
                .threadsAllowedToBlockForConnectionMultiplier(getThreadsAllowedToBlockForConnectionMultiplier())
                .maxWaitTime(getMaxWaitTime())
                .maxConnectionLifeTime(getMaxConnectionLifeTime())
                .connectTimeout(getConnectTimeout())
                .socketTimeout(getSocketTimeout())
                .socketKeepAlive(isSocketKeepAlive())
                .build();
    }


    public String getHosts() {
        return hosts;
    }

    public void setHosts(String hosts) {
        this.hosts = hosts;
    }

    public String getPorts() {
        return ports;
    }

    public void setPorts(String ports) {
        this.ports = ports;
    }

    public String getUserNames() {
        return userNames;
    }

    public void setUserNames(String userNames) {
        this.userNames = userNames;
    }

    public String getPasswords() {
        return passwords;
    }

    public void setPasswords(String passwords) {
        this.passwords = passwords;
    }

    public String getAuthDatabases() {
        return authDatabases;
    }

    public void setAuthDatabases(String authDatabases) {
        this.authDatabases = authDatabases;
    }

    @Override
    public String getDefaultDatabase() {
        return defaultDatabase;
    }

    public void setDefaultDatabase(String defaultDatabase) {
        this.defaultDatabase = defaultDatabase;
    }

    public int getMinConnectionsPerHost() {
        return minConnectionsPerHost;
    }

    public void setMinConnectionsPerHost(int minConnectionsPerHost) {
        this.minConnectionsPerHost = minConnectionsPerHost;
    }

    public int getConnectionsPerHost() {
        return connectionsPerHost;
    }

    public void setConnectionsPerHost(int connectionsPerHost) {
        this.connectionsPerHost = connectionsPerHost;
    }

    public int getThreadsAllowedToBlockForConnectionMultiplier() {
        return threadsAllowedToBlockForConnectionMultiplier;
    }

    public void setThreadsAllowedToBlockForConnectionMultiplier(int threadsAllowedToBlockForConnectionMultiplier) {
        this.threadsAllowedToBlockForConnectionMultiplier = threadsAllowedToBlockForConnectionMultiplier;
    }

    public int getMaxWaitTime() {
        return maxWaitTime;
    }

    public void setMaxWaitTime(int maxWaitTime) {
        this.maxWaitTime = maxWaitTime;
    }

    public int getMaxConnectionLifeTime() {
        return maxConnectionLifeTime;
    }

    public void setMaxConnectionLifeTime(int maxConnectionLifeTime) {
        this.maxConnectionLifeTime = maxConnectionLifeTime;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public boolean isSocketKeepAlive() {
        return socketKeepAlive;
    }

    public void setSocketKeepAlive(boolean socketKeepAlive) {
        this.socketKeepAlive = socketKeepAlive;
    }

    public void setMongoConverter(MongoConverter mongoConverter) {
        this.mongoConverter = mongoConverter;
    }

    @Override
    public MongoConverter getMongoConverter() {
        return mongoConverter;
    }

}
