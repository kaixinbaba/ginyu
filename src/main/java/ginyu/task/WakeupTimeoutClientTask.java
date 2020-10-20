package ginyu.task;

import ginyu.common.Consoles;
import ginyu.core.ClientTimeoutWrapper;
import ginyu.core.Server;
import ginyu.db.Database;
import ginyu.event.BlockEvent;
import ginyu.protocol.BulkStrings;
import ginyu.utils.BlockUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: junjiexun
 * @date: 2020/10/14 10:09 下午
 * @description:
 */
public class WakeupTimeoutClientTask {

    private final Timer timer = new Timer("ginyu-wakeup-timeout", true);

    private final Lock lock = new ReentrantLock();

    public void start() {
        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    if (!WakeupTimeoutClientTask.this.lock.tryLock()) {
                        Consoles.debug("上一轮还没结束，本次轮空");
                        return;
                    }
                    Long now = System.currentTimeMillis();
                    for (Database database : Server.INSTANCE.getDb().getDatabases()) {
                        Set<ClientTimeoutWrapper> unblock = new HashSet<>();
                        for (ClientTimeoutWrapper timeout : database.getTimeoutSets()) {
                            if (timeout.getTimeoutTimestamp() > now) {
                                return;
                            }
                            BlockEvent blockEvent = timeout.getBlockEvent();
                            // double check
                            if (!blockEvent.getClient().stillBlocked()) {
                                unblock.add(timeout);
                                continue;
                            }
                            // response null to client
                            blockEvent.getClient().getCtx().writeAndFlush(BulkStrings.NULL);
                            BlockUtils.clearBlockClient(blockEvent);
                            unblock.add(timeout);
                        }
                        if (!unblock.isEmpty()) {
                            database.removeTimeout(unblock);
                        }
                    }
                } finally {
                    WakeupTimeoutClientTask.this.lock.unlock();
                }

            }
        }, 5000, 1000);
    }

}
