package cmd.hash;

import cmd.AbstractRedisCommand;
import cmd.Command;
import common.Attributes;
import core.Client;
import core.Server;
import db.Database;
import io.netty.channel.ChannelHandlerContext;
import object.HashObject;
import object.ObjectType;
import protocol.*;
import utils.ProtocolValueUtils;

/**
 * @author: junjiexun
 * @date: 2020/10/13 1:56 下午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "hexists")
public class HExists extends AbstractRedisCommand<HExistsArg> {
    @Override
    public HExistsArg createArg(Arrays arrays) {
        return new HExistsArg(
                ProtocolValueUtils.getFromBulkStringsInArrays(arrays, 1),
                ProtocolValueUtils.getFromBulkStringsInArrays(arrays, 2));
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        Validates.validateArraysSize(commandName, arrays, 3);
    }

    @Override
    protected Resp2 doCommand0(HExistsArg arg, ChannelHandlerContext ctx) {
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
        String value = hashObject.getOriginal().get(arg.getField());
        if (value == null) {
            return Integers.ZERO;
        }
        return Integers.ONE;
    }
}
