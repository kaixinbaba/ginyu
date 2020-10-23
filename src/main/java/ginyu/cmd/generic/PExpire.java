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
@Command(name = "pexpire")
public class PExpire extends AbstractRedisCommand<PExpireArg, Integers> {

    @Override
    public PExpireArg createArg(Arrays arrays) {
        return new PExpireArg(
                ProtocolValueUtils.getFromBulkStringsInArrays(arrays, 1),
                ProtocolValueUtils.getLongFromBulkStringsInArrays(arrays, 2)
        );
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        Validates.validateArraysSize(commandName, arrays, 3);
        Validates.validateLong(arrays, 2, "milliseconds");
    }

    @Override
    protected Resp2 doCommand0(PExpireArg arg, ChannelHandlerContext ctx) {
        Client client = Attributes.getClient(ctx);
        Database database = client.getDatabase();
        RedisObject redisObject = database.get(arg.getKey());
        if (redisObject == null) {
            return Integers.ZERO;
        }
        database.setExpired(arg.getKey(), System.currentTimeMillis() + arg.getMilliseconds());
        return Integers.ONE;
    }
}
