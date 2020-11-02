package ginyu.task;

import ginyu.core.Server;
import ginyu.db.Database;
import ginyu.db.Db;
import ginyu.object.Dict;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author: junjiexun
 * @date: 2020/10/14 10:09 下午
 * @description:
 */
public class CleanExpiredTask {

    private final Timer timer = new Timer("ginyu-clean-expire", true);

    public void start() {
        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Db db = Server.INSTANCE.getDb();
                long now = System.currentTimeMillis();
                for (Database database : db.getDatabases()) {
                    Dict<String, Long> expired = database.getExpired();
                    List<Map.Entry<String, Long>> entryList = new ArrayList<>(expired.entrySet());
                    entryList.sort(Comparator.comparingLong(Map.Entry::getValue));
                    for (Map.Entry<String, Long> entry : entryList) {
                        if (now >= entry.getValue()) {
                            // delete
                            database.delete(entry.getKey());
                        } else {
                            // 只要出现第一个没过期的就退出，因为此时的entry都是排序过的
                            break;
                        }
                    }
                }
            }
        }, 3000, 5000);
    }

}
