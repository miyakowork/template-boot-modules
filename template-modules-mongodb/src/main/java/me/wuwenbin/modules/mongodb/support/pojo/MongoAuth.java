package me.wuwenbin.modules.mongodb.support.pojo;

/**
 * Created by wuwenbin on 2017/4/22.
 */
public class MongoAuth {
    private String username;
    private String password;
    private String authDatabase;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthDatabase() {
        return authDatabase;
    }

    public void setAuthDatabase(String authDatabase) {
        this.authDatabase = authDatabase;
    }
}
