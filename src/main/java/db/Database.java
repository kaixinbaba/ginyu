package db;

import lombok.Getter;
import object.Dict;
import object.RedisObject;
import object.StringObject;

import java.util.concurrent.ConcurrentSkipListMap;

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
    private final ConcurrentSkipListMap<String, Long> expired;

    public Database(Integer id) {
        this.id = id;
        this.dict = new Dict<>();
        this.expired = new ConcurrentSkipListMap<>();
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
            this.expired.remove(key);
            if (value != null) {
                deleted++;
            }
        }
        return deleted;
    }

    public boolean checkIfExpired(String key) {
        Long now = System.currentTimeMillis();
        Long expiredTimestamp = expired.get(key);
        if (expiredTimestamp != null && now >= expiredTimestamp) {
            return true;
        }
        return false;
    }

    public Integer cleanExpired(String key) {
        return expired.remove(key) == null ? 0 : 1;
    }

    public int exists(String... keys) {
        int exists = 0;
        for (String key : keys) {
            if (this.dict.containsKey(key)) {
                exists++;
            }
        }
        return exists;
    }
}
