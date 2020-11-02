package ginyu.db;

import ginyu.core.ClientTimeoutWrapper;
import ginyu.core.Snapshot;
import ginyu.event.BlockEvent;
import ginyu.object.Dict;
import ginyu.object.RedisObject;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author: junjiexun
 * @date: 2020/10/13 10:03 下午
 * @description:
 */
@SuppressWarnings("all")
public class Database implements Snapshot {

    @Getter
    @Setter
    private Integer id;

    @Getter
    @Setter
    private Dict<String, RedisObject> dict;

    @Getter
    @Setter
    private Dict<String, Long> expired;

    private Dict<String, ConcurrentSkipListSet<BlockEvent>> blockingDict;

    @Getter
    @Setter
    private ConcurrentSkipListSet<ClientTimeoutWrapper> timeoutSets;

    public Database() {
        this.blockingDict = new Dict<>();
    }

    public Database(Integer id) {
        this.id = id;
        this.dict = new Dict<>();
        this.expired = new Dict<>();
        this.blockingDict = new Dict<>();
        this.timeoutSets = new ConcurrentSkipListSet<>();
    }

    public Database(Database database) {
        this.id = database.id;
        this.dict = new Dict<>(database.getDict());
        this.expired = new Dict<>(database.getExpired());
        this.blockingDict = new Dict<>();
        this.timeoutSets = new ConcurrentSkipListSet<>();
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

    public void addToBlockingDict(String key, BlockEvent blockEvent) {
        ConcurrentSkipListSet<BlockEvent> clients = this.blockingDict.get(key);
        if (clients == null) {
            synchronized (this.blockingDict) {
                clients = this.blockingDict.get(key);
                if (clients == null) {
                    clients = new ConcurrentSkipListSet<>();
                    this.blockingDict.put(key, clients);
                }
            }
        }
        clients.add(blockEvent);
    }

    public Set<BlockEvent> getBlockedEvents(String key) {
        return this.blockingDict.get(key);
    }

    public void addToTimeoutSet(ClientTimeoutWrapper clientTimeoutWrapper) {
        this.timeoutSets.add(clientTimeoutWrapper);
    }

    public void removeTimeout(Set<ClientTimeoutWrapper> unblock) {
        this.timeoutSets.removeAll(unblock);
    }

    public void deleteIfNeeded(String key) {
        RedisObject redisObject = this.dict.get(key);
        if (redisObject == null) {
            return;
        }
        if (redisObject.isEmptyValue()) {
            this.delete(key);
        }
    }

    public void removeBlockKey(String blockKey) {
        this.blockingDict.remove(blockKey);
    }

    @Override
    public String toSnapshot() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.id);
        sb.append(this.dict.toSnapshot());
        sb.append(this.expired.toSnapshot());
        return sb.toString();
    }
}
