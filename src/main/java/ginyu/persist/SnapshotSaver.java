package ginyu.persist;

import com.alibaba.fastjson.JSONObject;
import ginyu.common.Consoles;
import ginyu.core.Server;
import ginyu.db.Db;
import ginyu.event.Events;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.function.Supplier;

import static ginyu.common.Constants.DEFAULT_SNAPSHOT_PATH;

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
            String saveContent = JSONObject.toJSONString(serverForSaver, true);
            String snapshotPath = Server.INSTANCE.getGinyuConfig().getSnapshotPath();
            if (snapshotPath == null || snapshotPath.isEmpty()) {
                snapshotPath = DEFAULT_SNAPSHOT_PATH;
            }
            try {
                FileUtils.writeStringToFile(new File(snapshotPath), saveContent, Charset.defaultCharset());
            } catch (IOException e) {
                Consoles.error(e.getMessage());
            }
            return null;
        });
    }

    @Override
    public void load(String filePath) throws IOException {
        if (filePath == null) {
            filePath = DEFAULT_SNAPSHOT_PATH;
        }
        File file = new File(filePath);
        if (!file.exists() || !file.canRead()) {
            Consoles.debug("The snapshot %s can't be read", file);
            return;
        }
        String snapshotJson = FileUtils.readFileToString(file, Charset.defaultCharset());
        ServerForSaver serverForSaver = JSONObject.parseObject(snapshotJson, ServerForSaver.class);
        Server.INSTANCE.loadFromSaver(serverForSaver);
    }
}
