package db;

import lombok.Getter;
import object.Dict;
import object.RedisObject;
import object.StringObject;

/**
 * @author: junjiexun
 * @date: 2020/10/13 10:03 下午
 * @description:
 */
@SuppressWarnings("all")
public class Database {

    @Getter
    private final Integer id;

    private final Dict<String, RedisObject> dict;

    private final Dict<String, Long> expired;

    public Database(Integer id) {
        this.id = id;
        this.dict = new Dict<>();
        this.expired = new Dict<>();
    }

    public RedisObject get(String key) {
        return dict.get(key);
    }

    public StringObject getString(String key) {
        return (StringObject) this.get(key);
    }

    public void setString(String key, StringObject object) {
        dict.put(key, object);
    }

    public void setExpired(String key, Long expiredTimestamp) {
        expired.put(key, expiredTimestamp);
    }
}
