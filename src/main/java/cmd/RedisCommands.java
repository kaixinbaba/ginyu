package cmd;

import utils.ReflectUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/**
 * @author: junjiexun
 * @date: 2020/10/13 2:01 下午
 * @description:
 */
@SuppressWarnings("all")
public abstract class RedisCommands {

    public static Map<String, RedisCommand> COMMAND_MAP;

    static {
        try {
            init();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    private static void init() throws IOException {
        COMMAND_MAP = Collections.unmodifiableMap(
                ReflectUtils.getRedisCommandFromPackage(RedisCommand.class.getPackage().getName()));
    }
}
