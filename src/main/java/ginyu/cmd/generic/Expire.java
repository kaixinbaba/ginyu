package ginyu.cmd.generic;

import ginyu.cmd.AbstractRedisCommand;
import ginyu.cmd.Command;
import ginyu.common.Attributes;
import ginyu.core.Client;
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
@Command(name = "expire")
public class Expire extends AbstractRedisCommand<ExpireArg, Integers> {

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
        Database database = client.getDatabase();
        RedisObject redisObject = database.get(arg.getKey());
        if (redisObject == null) {
            return Integers.ZERO;
        }
        database.setExpired(arg.getKey(), System.currentTimeMillis() + arg.getSeconds() * 1000);
        return Integers.ONE;
    }
}
