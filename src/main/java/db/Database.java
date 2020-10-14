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

    @Getter
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

    public int delete(String... keys) {
        int deleted = 0;
        for (String key : keys) {
            RedisObject value = this.dict.remove(key);
            if (value != null) {
                this.expired.remove(key);
                deleted++;
            }
        }
        return deleted;
    }

    public boolean checkIfExpired(String key) {
        Long now = System.currentTimeMillis();
        Long expiredTimestamp = expired.get(key);
        if (expiredTimestamp != null && now >= expiredTimestamp) {
            this.delete(key);
            return true;
        }
        return false;
    }
}
