package ginyu.persist;

import com.alibaba.fastjson.JSONObject;
import ginyu.common.Consoles;
import ginyu.core.Server;
import ginyu.db.Db;
import ginyu.event.Events;

import java.util.function.Supplier;

/**
 * @author: junjiexun
 * @date: 2020/10/27 9:47 下午
 * @description:
 */
public class SnapshotSaver implements Saver {

    @Override
    public void save() {
        Events.post((Supplier<Void>) () -> {
            final Server server = Server.INSTANCE;
            ServerForSaver serverForSaver = new ServerForSaver();
            serverForSaver.setDb(new Db(server.getDb()));
            String s = JSONObject.toJSONString(serverForSaver, true);
            Consoles.info(s);
            return null;
        });
    }

    @Override
    public void load(String filePath) {

    }
}
