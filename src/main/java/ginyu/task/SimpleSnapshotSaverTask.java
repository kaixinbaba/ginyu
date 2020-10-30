package ginyu.task;

import ginyu.common.Consoles;
import ginyu.core.Server;
import ginyu.db.Database;
import ginyu.db.Db;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author: junjiexun
 * @date: 2020/10/14 10:09 下午
 * @description:
 */
public class SimpleSnapshotSaverTask {

    private final Timer timer = new Timer("ginyu-snapshto-save", true);

    public void start() {
        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Consoles.info("Try to save snapshot...");
                Server.INSTANCE.save();
            }
        }, 3000, 1000 * 20);
    }

}
