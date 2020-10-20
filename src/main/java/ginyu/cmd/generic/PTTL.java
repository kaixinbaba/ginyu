package ginyu.cmd.generic;

import ginyu.cmd.AbstractRedisCommand;
import ginyu.cmd.Command;
import ginyu.common.Attributes;
import ginyu.core.Client;
import ginyu.core.Server;
import ginyu.db.Database;
import ginyu.object.RedisObject;
import ginyu.protocol.Arrays;
import ginyu.protocol.Integers;
import ginyu.protocol.Resp2;
import ginyu.protocol.Validates;
import ginyu.utils.ProtocolValueUtils;
import io.netty.channel.ChannelHandlerContext;

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
        Database database = client.getDatabase();
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
