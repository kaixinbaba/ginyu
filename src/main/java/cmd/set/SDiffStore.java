package cmd.set;

import cmd.AbstractRedisCommand;
import cmd.Command;
import common.Attributes;
import core.Client;
import core.Server;
import db.Database;
import io.netty.channel.ChannelHandlerContext;
import object.ObjectType;
import object.RedisObject;
import object.SetObject;
import protocol.Arrays;
import protocol.Integers;
import protocol.Resp2;
import protocol.Validates;
import utils.ConvertUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static common.Constants.STR_EMPTY_ARRAY;
import static object.SetObject.NONE;

/**
 * @author: junjiexun
 * @date: 2020/10/13 1:56 下午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "sdiffstore")
public class SDiffStore extends AbstractRedisCommand<SDiffStoreArg, Integers> {

    @Override
    public SDiffStoreArg createArg(Arrays arrays) {
        List<String> args = arrays.map2String(true);
        return new SDiffStoreArg(
                // 上面的转换已经删除了命令本身，所以第一个字符串就是destination
                args.remove(0),
                args.toArray(STR_EMPTY_ARRAY)
        );
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        Validates.validateArraysSize(commandName, arrays, 3, null);
    }

    @Override
    protected Resp2 doCommand0(SDiffStoreArg arg, ChannelHandlerContext ctx) {
        Client client = Attributes.getClient(ctx);
        Database database = Server.INSTANCE.getDb().getDatabase(client.getDb());
        SetObject setObject = Validates.validateType(database.get(arg.getKeys()[0]), ObjectType.SET);
        if (setObject == null) {
            return Integers.ZERO;
        }
        Set<String> first = new HashSet<>(setObject.getOriginal().keySet());
        for (int i = 1; i < arg.getKeys().length; i++) {
            SetObject afterObject = Validates.validateType(database.get(arg.getKeys()[i]), ObjectType.SET);
            if (afterObject != null) {
                first.removeAll(afterObject.getOriginal().keySet());
            }
        }
        if (!first.isEmpty()) {
            SetObject destination = new SetObject();
            destination.getOriginal().putAll(ConvertUtils.set2map(first, NONE));
            RedisObject oldValue = database.set(arg.getDestination(), destination);
            if (oldValue != null) {
                // 有覆盖原来的key，所以要同时删除expired（不一定有，但是要检查下）
                database.cleanExpired(arg.getDestination());
            }
        }
        return Integers.create(first.size());
    }
}
