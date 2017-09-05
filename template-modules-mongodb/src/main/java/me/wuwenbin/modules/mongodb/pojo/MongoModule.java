package me.wuwenbin.modules.mongodb.pojo;

import org.springframework.data.annotation.Id;

import java.util.UUID;

/**
 * <b>author</b>: 伍文彬<br>
 * <b>date</b>: 2016年5月23日<br>
 * <b>time</b>: 下午12:04:20<br>
 * <b>ClassName</b>: ObjectModule<br>
 * <b>Description</b>: 所有的实体类都要继承此类，以来生成MongoDB的主键_id<br>
 * <b>Version</b>: Ver 1.0.0<br>
 */
public class MongoModule {

    @Id
    public String _id;

    public MongoModule() {
        this.set_id(String.valueOf(UUID.randomUUID().toString().replace("-", "")));
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
