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

import java.util.ArrayList;
import java.util.List;

/**
 * @author: junjiexun
 * @date: 2020/10/16 10:41 上午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "hmget")
public class HMGet extends AbstractRedisCommand<HMGetArg> {
    @Override
    protected HMGetArg createArg(Arrays arrays) {
        List<String> argList = arrays.map2String(true);
        return new HMGetArg(
                argList.remove(0),
                argList.toArray(Constants.STR_EMPTY_ARRAY)
        );
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        Validates.validateArraysSize(commandName, arrays, 3, null);
    }

    @Override
    protected Resp2 doCommand0(HMGetArg arg, ChannelHandlerContext ctx) {
        Client client = Attributes.getClient(ctx);
        Database database = Server.INSTANCE.getDb().getDatabase(client.getDb());
        boolean expired = database.checkIfExpired(arg.getKey());
        if (expired) {
            database.delete(arg.getKey());
            return Arrays.createSpecifiedSizeWithNull(arg.getFields().length);
        }
        HashObject hashObject = Validates.validateType(database.get(arg.getKey()), ObjectType.HASH);
        if (hashObject == null) {
            return Arrays.createSpecifiedSizeWithNull(arg.getFields().length);
        }
        List<String> data = new ArrayList<>(arg.getFields().length);
        for (String field : arg.getFields()) {
            data.add(hashObject.getOriginal().get(field));
        }
        return Arrays.createByStringList(data);
    }
}
