package ginyu.db;

import ginyu.object.Dict;
import ginyu.object.RedisObject;
import lombok.Getter;

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
        return this.dict.get(key);
    }

    public RedisObject set(String key, RedisObject object) {
        return this.dict.put(key, object);
    }

    public void setExpired(String key, Long expiredTimestamp) {
        this.expired.put(key, expiredTimestamp);
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
        Long expiredTimestamp = this.expired.get(key);
        if (expiredTimestamp != null && now >= expiredTimestamp) {
            return true;
        }
        return false;
    }

    public Integer cleanExpired(String key) {
        return this.expired.remove(key) == null ? 0 : 1;
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

    public Long getExpired(String key) {
        return this.expired.get(key);
    }

    public RedisObject remove(String key) {
        return this.dict.remove(key);
    }

    public Long removeExpire(String key) {
        return this.expired.remove(key);
    }
}
