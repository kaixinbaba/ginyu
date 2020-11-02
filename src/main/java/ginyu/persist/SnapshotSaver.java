package ginyu.persist;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.FieldInfo;
import ginyu.common.Consoles;
import ginyu.core.Server;
import ginyu.db.Db;
import ginyu.event.Events;
import ginyu.object.RedisObject;
import ginyu.object.StringObject;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.function.Supplier;

import static ginyu.common.Constants.DEFAULT_SNAPSHOT_PATH;

/**
 * @author: junjiexun
 * @date: 2020/10/27 9:47 下午
 * @description:
 */
public class SnapshotSaver implements Saver {

    private final ParserConfig parserConfig = new ParserConfig() {
        @Override
        public ObjectDeserializer getDeserializer(FieldInfo fieldInfo) {
            Consoles.info("{}", fieldInfo);
            return super.getDeserializer(fieldInfo);
        }

        @Override
        public ObjectDeserializer getDeserializer(Type type) {
            if (type == RedisObject.class) {
                return super.getDeserializer(StringObject.class);
            }
            return super.getDeserializer(type);
        }
    };

    @Override
    public synchronized void save() {
        Events.post((Supplier<Void>) () -> {
            final Server server = Server.INSTANCE;
            ServerForSaver serverForSaver = new ServerForSaver();
            serverForSaver.setDb(new Db(server.getDb()));
            return null;
        });
    }

    @Override
    public synchronized void load(String filePath) throws IOException {
        if (filePath == null) {
            filePath = DEFAULT_SNAPSHOT_PATH;
        }
        File file = new File(filePath);
        if (!file.exists() || !file.canRead()) {
            Consoles.debug("The snapshot {} can't be read", file);
            return;
        }
        String snapshotJson = FileUtils.readFileToString(file, Charset.defaultCharset());
        ServerForSaver serverForSaver = JSONObject.parseObject(snapshotJson, ServerForSaver.class, parserConfig);
        Server.INSTANCE.loadFromSaver(serverForSaver);
    }
}
