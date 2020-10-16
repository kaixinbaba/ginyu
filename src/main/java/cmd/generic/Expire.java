package cmd.generic;

import cmd.AbstractRedisCommand;
import cmd.Command;
import common.Attributes;
import core.Client;
import core.Server;
import db.Database;
import io.netty.channel.ChannelHandlerContext;
import object.RedisObject;
import protocol.Arrays;
import protocol.Integers;
import protocol.Resp2;
import protocol.Validates;
import utils.ProtocolValueUtils;

/**
 * @author: junjiexun
 * @date: 2020/10/13 1:56 下午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "expire")
public class Expire extends AbstractRedisCommand<ExpireArg> {

    @Override
    public ExpireArg createArg(Arrays arrays) {
        return new ExpireArg(
                ProtocolValueUtils.getFromBulkStringsInArrays(arrays, 1),
                ProtocolValueUtils.getIntFromBulkStringsInArrays(arrays, 2)
        );
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        Validates.validateArraysSize(commandName, arrays, 3);
        Validates.validateInteger(arrays, 2, "seconds");
    }

    @Override
    protected Resp2 doCommand0(ExpireArg arg, ChannelHandlerContext ctx) {
        Client client = Attributes.getClient(ctx);
        Database database = Server.INSTANCE.getDb().getDatabase(client.getDb());
        RedisObject redisObject = database.get(arg.getKey());
        if (redisObject == null) {
            return Integers.ZERO;
        }
        database.setExpired(arg.getKey(), System.currentTimeMillis() + arg.getSeconds() * 1000);
        return Integers.ONE;
    }
}
