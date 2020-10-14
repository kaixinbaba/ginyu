package cmd.string;

import cmd.AbstractRedisCommand;
import cmd.Command;
import com.sun.org.apache.bcel.internal.generic.ObjectType;
import common.Attributes;
import core.Server;
import db.Database;
import exception.CommandValidateException;
import exception.SetWrongTypeException;
import io.netty.channel.ChannelHandlerContext;
import object.RedisObject;
import object.StringObject;
import protocol.*;

import java.util.List;

/**
 * @author: junjiexun
 * @date: 2020/10/13 5:48 下午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "set")
public class Set extends AbstractRedisCommand<SetArg> {

    private static final int XX = 0 << 1;
    private static final int NX = 0 << 2;

    /**
     * SET key value [EX seconds|PX milliseconds] [NX|XX] [KEEPTTL]
     *
     * @param arrays
     * @return
     */
    @Override
    protected SetArg createArg(Arrays arrays) {
        List<String> argList = arrays.map2String();
        String key = argList.get(1);
        String value = argList.get(2);
        boolean xx = false;
        boolean nx = false;
        long expiredSeconds = 0;
        long expiredMilliSeconds = 0;
        if (argList.size() > 3) {
            for (int i = 3; i < argList.size(); ) {
                String arg = argList.get(i);
                if (arg.equalsIgnoreCase("EX")) {
                    expiredSeconds = getExpiredTime("EX", i, argList);
                    i += 2;
                } else if (arg.equalsIgnoreCase("PX")) {
                    expiredMilliSeconds = getExpiredTime("PX", i, argList);
                    i += 2;
                } else if (arg.equalsIgnoreCase("XX")) {
                    xx = true;
                    i++;
                } else if (arg.equalsIgnoreCase("NX")) {
                    nx = true;
                    i++;
                }
            }
        }
        SetArg setArg = new SetArg();
        setArg.setKey(key);
        setArg.setValue(value);
        if (expiredMilliSeconds > 0) {
            // 优先毫秒
            setArg.setExpiredMilliSeconds(expiredMilliSeconds);
        } else if (expiredSeconds > 0) {
            // 其次秒
            setArg.setExpiredMilliSeconds(expiredSeconds * 1000);
        }
        setArg.setXx(xx);
        setArg.setNx(nx);
        return setArg;
    }

    private Long getExpiredTime(String flag, int i, List<String> argList) {
        try {
            return Long.valueOf(argList.get(i + 1));
        } catch (NumberFormatException n) {
            throw new CommandValidateException("%s argument must be an integer", flag);
        }
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        Validates.validateArraysSize(commandName, arrays, 3, 9);
    }

    private Resp2 set(Database database, String key, String value, Long expiredMilliSeconds) {
        StringObject stringObject = new StringObject();
        stringObject.setOriginal(value);
        database.setString(key, stringObject);
        if (expiredMilliSeconds != null && expiredMilliSeconds > 0) {
            database.setExpired(key, System.currentTimeMillis() + expiredMilliSeconds);
        }
        return SimpleStrings.OK;
    }

    @Override
    protected Resp2 doCommand0(SetArg arg, ChannelHandlerContext ctx) {
        Database database = Server.INSTANCE.getDb().getDatabase(Attributes.getClient(ctx).getDb());
        RedisObject redisObject = database.get(arg.getKey());
        if (redisObject == null) {
            if (arg.getXx()) {
                return BulkStrings.NULL;
            }
            return set(database, arg.getKey(), arg.getValue(), arg.getExpiredMilliSeconds());
        } else {
            if (arg.getNx()) {
                return BulkStrings.NULL;
            }
            if (!(redisObject instanceof StringObject)) {
                throw new SetWrongTypeException(
                        "WRONGTYPE Operation against a key holding the wrong kind of value, expect %s but %s",
                        ObjectType.STRING, redisObject.getType()
                );
            }
            return set(database, arg.getKey(), arg.getValue(), arg.getExpiredMilliSeconds());
        }
    }
}
