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
@Command(name = "pttl", checkExpire = true)
public class PTTL extends AbstractRedisCommand<PTTLArg, Integers> {
    @Override
    public PTTLArg createArg(Arrays arrays) {
        return new PTTLArg(ProtocolValueUtils.getFromBulkStringsInArrays(arrays, 1));
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        Validates.validateArraysSize(commandName, arrays, 2);
    }

    @Override
    protected Resp2 doCommand0(PTTLArg arg, ChannelHandlerContext ctx) {
        Client client = Attributes.getClient(ctx);
        Database database = Server.INSTANCE.getDb().getDatabase(client.getDb());
        RedisObject value = database.get(arg.getKey());
        if (value == null) {
            return Integers.N_TWO;
        }
        Long expiredTimestamp = database.getExpired(arg.getKey());
        if (expiredTimestamp == null) {
            return Integers.N_ONE;
        }
        return Integers.create(expiredTimestamp - System.currentTimeMillis());
    }
}
