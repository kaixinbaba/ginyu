package cmd.set;

import cmd.AbstractRedisCommand;
import cmd.Command;
import common.Attributes;
import core.Client;
import core.Server;
import db.Database;
import io.netty.channel.ChannelHandlerContext;
import object.ObjectType;
import object.SetObject;
import protocol.Arrays;
import protocol.Resp2;
import protocol.Validates;

import java.util.HashSet;
import java.util.Set;

import static common.Constants.STR_EMPTY_ARRAY;

/**
 * @author: junjiexun
 * @date: 2020/10/13 1:56 下午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "sinter")
public class SInter extends AbstractRedisCommand<SInterArg> {

    @Override
    public SInterArg createArg(Arrays arrays) {
        return new SInterArg(arrays.map2String(true).toArray(STR_EMPTY_ARRAY));
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        Validates.validateArraysSize(commandName, arrays, 2, null);
    }

    @Override
    protected Resp2 doCommand0(SInterArg arg, ChannelHandlerContext ctx) {
        Client client = Attributes.getClient(ctx);
        Database database = Server.INSTANCE.getDb().getDatabase(client.getDb());
        SetObject setObject = Validates.validateType(database.get(arg.getKeys()[0]), ObjectType.SET);
        if (setObject == null) {
            return Arrays.EMPTY;
        }
        Set<String> first = new HashSet<>(setObject.getOriginal().keySet());
        for (int i = 0; i < arg.getKeys().length; i++) {
            SetObject afterObject = Validates.validateType(database.get(arg.getKeys()[i]), ObjectType.SET);
            if (first.isEmpty() || afterObject == null || afterObject.getOriginal().isEmpty()) {
                // 只要一个key不存在 或者 空集 就直接返回空
                return Arrays.EMPTY;
            }
            first.retainAll(afterObject.getOriginal().keySet());
        }
        return Arrays.createByStringCollection(first);
    }
}