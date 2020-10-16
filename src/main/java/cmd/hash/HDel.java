package cmd.hash;

import cmd.AbstractRedisCommand;
import cmd.Command;
import common.Attributes;
import common.Constants;
import core.Client;
import core.Server;
import db.Database;
import io.netty.channel.ChannelHandlerContext;
import object.HashObject;
import object.ObjectType;
import protocol.Arrays;
import protocol.Integers;
import protocol.Resp2;
import protocol.Validates;

import java.util.List;

/**
 * @author: junjiexun
 * @date: 2020/10/16 10:41 上午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "hdel")
public class HDel extends AbstractRedisCommand<HDelArg> {
    @Override
    protected HDelArg createArg(Arrays arrays) {
        List<String> argList = arrays.map2String(true);
        return new HDelArg(
                argList.remove(0),
                argList.toArray(Constants.STR_EMPTY_ARRAY)
        );
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        Validates.validateArraysSize(commandName, arrays, 3, null);
    }

    @Override
    protected Resp2 doCommand0(HDelArg arg, ChannelHandlerContext ctx) {
        Client client = Attributes.getClient(ctx);
        Database database = Server.INSTANCE.getDb().getDatabase(client.getDb());
        boolean expired = database.checkIfExpired(arg.getKey());
        if (expired) {
            database.delete(arg.getKey());
            return Integers.ZERO;
        }
        HashObject hashObject = Validates.validateType(database.get(arg.getKey()), ObjectType.HASH);
        if (hashObject == null) {
            return Integers.ZERO;
        }
        int deleted = 0;
        for (String field : arg.getFields()) {
            String value = hashObject.getOriginal().remove(field);
            if (value != null) {
                deleted++;
            }
        }
        return Integers.create(deleted);
    }
}
