package ginyu.persist;

import com.google.common.eventbus.Subscribe;
import ginyu.common.Consoles;
import ginyu.core.Client;
import ginyu.core.ClientTimeoutWrapper;
import ginyu.db.Database;
import ginyu.event.BlockEvent;
import ginyu.event.list.BlockByBPopEvent;
import ginyu.event.list.PushEvent;
import ginyu.object.ListObject;
import ginyu.object.ObjectType;
import ginyu.protocol.Arrays;
import ginyu.protocol.Resp2;
import ginyu.protocol.Validates;
import ginyu.utils.BlockUtils;
import ginyu.utils.Dates;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @author: junjiexun
 * @date: 2020/10/20 1:41 下午
 * @description:
 */
@SuppressWarnings("all")
public class SnapshotSaverListener {

    @Subscribe
    public void saveSnapshot(Supplier<Void> supplier) {
        supplier.get();
        Consoles.info("{} save done", Dates.date2string(new Date(), Dates.PTTERN_DATETIME));
    }
}
