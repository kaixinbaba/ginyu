package ginyu.cmd.list;

import com.google.common.eventbus.Subscribe;
import ginyu.common.Attributes;
import ginyu.core.Client;
import ginyu.core.Server;
import ginyu.db.Database;
import ginyu.object.ListObject;
import ginyu.object.ObjectType;
import ginyu.protocol.Arrays;
import ginyu.protocol.Validates;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author: junjiexun
 * @date: 2020/10/19 4:54 下午
 * @description: 订阅中心，用于解决BPop订阅等待的问题
 */
@SuppressWarnings("all")
public class ListSubscriptionCenter {

    private final ConcurrentHashMap<String, ConcurrentSkipListSet<BPopEvent>> key2watcher = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<BPopEvent, ConcurrentSkipListSet<String>> watcher2key = new ConcurrentHashMap<>();

    @Subscribe
    public void bPopSubscribe(BPopEvent event) {
        for (String key : event.getKeys()) {
            ConcurrentSkipListSet<BPopEvent> bPopEvents = key2watcher.get(key);
            if (bPopEvents == null) {
                bPopEvents = new ConcurrentSkipListSet<>();
                key2watcher.put(key, bPopEvents);
            }
            bPopEvents.add(event);
        }
        ConcurrentSkipListSet<String> keySet = watcher2key.get(event);
        if (keySet == null) {
            keySet = new ConcurrentSkipListSet<>();
            watcher2key.put(event, keySet);
        }
        Collections.addAll(keySet, event.getKeys());
    }


    private void cleanSubsribe(Set<BPopEvent> popSuccessful,
                               Set<BPopEvent> bPopEvents,
                               String key) {
        for (BPopEvent bPopEvent : popSuccessful) {
            bPopEvents.remove(bPopEvent);
            ConcurrentSkipListSet<String> otherKeys = watcher2key.remove(bPopEvent);
            if (otherKeys != null && !otherKeys.isEmpty()) {
                for (String otherKey : otherKeys) {
                    if (otherKey.equals(key)) {
                        // 排除掉当前的key
                        continue;
                    }
                    ConcurrentSkipListSet<BPopEvent> bPopEventsInOtherKey = key2watcher.get(otherKey);
                    if (bPopEventsInOtherKey != null && !bPopEventsInOtherKey.isEmpty()) {
                        bPopEventsInOtherKey.remove(bPopEvent);
                        if (bPopEventsInOtherKey.isEmpty()) {
                            key2watcher.remove(otherKey);
                        }
                    }
                }
            }
        }
        if (bPopEvents.isEmpty()) {
            // 如果key对应的客户端都成功pop了，这个key就可以删除
            key2watcher.remove(key);
        }
    }

    @Subscribe
    public void pushNotify(PushEvent event) {
        if (!key2watcher.containsKey(event.getKey())) {
            return;
        }
        ConcurrentSkipListSet<BPopEvent> bPopEvents = key2watcher.get(event.getKey());
        Set<BPopEvent> popSuccessful = new HashSet<>();
        int i = 0;
        for (BPopEvent bPopEvent : bPopEvents) {
            if (i >= event.getSize()) {
                break;
            }
            Client client = Attributes.getClient(bPopEvent.getCtx());
            Database database = Server.INSTANCE.getDb().getDatabase(client.getDb());
            ListObject listObject = Validates.validateType(database.get(event.getKey()), ObjectType.LIST);
            if (listObject == null) {
                continue;
            }
            String value = listObject.getOriginal().pop(bPopEvent.getIsLeft());
            if (value != null) {
                // pop成功，解除监听
                popSuccessful.add(bPopEvent);
                bPopEvent.getCtx().writeAndFlush(Arrays.createByStringArray(event.getKey(), value));
            }
            i++;
        }
        if (!popSuccessful.isEmpty()) {
            this.cleanSubsribe(popSuccessful, bPopEvents, event.getKey());
        }
    }
}
