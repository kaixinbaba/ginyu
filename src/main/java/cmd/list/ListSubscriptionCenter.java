package cmd.list;

import com.google.common.eventbus.Subscribe;

import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author: junjiexun
 * @date: 2020/10/19 4:54 下午
 * @description: 订阅中心，用于解决BPop订阅等待的问题
 */
@SuppressWarnings("all")
public class ListSubscriptionCenter {

    private final ConcurrentHashMap<String, ConcurrentSkipListSet<BPopWatcher>> key2watcher = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<BPopWatcher, ConcurrentSkipListSet<String>> watcher2key = new ConcurrentHashMap<>();

    @Subscribe
    public void test(String name) {
        System.out.println(name);
    }

    public void subscribe(BPopWatcher watcher, String... keys) {
        for (String key : keys) {
            ConcurrentSkipListSet<BPopWatcher> bPopWatchers = key2watcher.get(key);
            if (bPopWatchers == null) {
                bPopWatchers = new ConcurrentSkipListSet<>();
                key2watcher.put(key, bPopWatchers);
            }
            bPopWatchers.add(watcher);
        }
        ConcurrentSkipListSet<String> keySet = watcher2key.get(watcher);
        if (keySet == null) {
            keySet = new ConcurrentSkipListSet<>();
            watcher2key.put(watcher, keySet);
        }
        Collections.addAll(keySet, keys);
    }

}
