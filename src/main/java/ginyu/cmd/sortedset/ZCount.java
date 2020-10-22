package ginyu.cmd.sortedset;

import ginyu.cmd.AbstractRedisCommand;
import ginyu.cmd.Command;
import ginyu.common.Attributes;
import ginyu.core.Client;
import ginyu.db.Database;
import ginyu.object.ObjectType;
import ginyu.object.ZSetObject;
import ginyu.protocol.Arrays;
import ginyu.protocol.Integers;
import ginyu.protocol.Resp2;
import ginyu.protocol.Validates;
import ginyu.utils.ProtocolValueUtils;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author: junjiexun
 * @date: 2020/10/16 2:54 下午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "zcount")
public class ZCount extends AbstractRedisCommand<ZCountArg, Integers> {

    @Override
    protected ZCountArg createArg(Arrays arrays) {
        return new ZCountArg(
                ProtocolValueUtils.getFromBulkStringsInArrays(arrays, 1),
                ProtocolValueUtils.getDoubleFromBulkStringsInArrays(arrays, 2),
                ProtocolValueUtils.getDoubleFromBulkStringsInArrays(arrays, 3)
        );
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        Validates.validateArraysSize(commandName, arrays, 4);
        Validates.validateDouble(arrays, 2, "min");
        Validates.validateDouble(arrays, 3, "max");
    }

    @Override
    protected Resp2 doCommand0(ZCountArg arg, ChannelHandlerContext ctx) {
        Client client = Attributes.getClient(ctx);
        Database database = client.getDatabase();
        ZSetObject zSetObject = Validates.validateType(database.get(arg.getKey()), ObjectType.ZSET);
        if (zSetObject == null) {
            return Integers.ZERO;
        }
        return Integers.create(zSetObject.getOriginal().countByScoreRange(arg.getMin(), arg.getMax()));
    }
}
