package task;

import core.Server;
import db.Database;
import db.Db;
import object.Dict;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author: junjiexun
 * @date: 2020/10/14 10:09 下午
 * @description:
 */
public class CleanExpiredTask {

    private final Timer timer = new Timer("ginyu-clean-expire", true);

    public void start() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Db db = Server.INSTANCE.getDb();
                long now = System.currentTimeMillis();
                for (Database database : db.getDatabases()) {
                    Dict<String, Long> expired = database.getExpired();
                    for (Map.Entry<String, Long> entry : expired.entrySet()) {
                        if (now >= entry.getValue()) {
                            // delete
                            database.delete(entry.getKey());
                        }
                    }
                }
            }
        }, 5000, 3000);
    }

}
