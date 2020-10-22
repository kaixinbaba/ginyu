package ginyu.cmd.sortedset;

import ginyu.cmd.AbstractRedisCommand;
import ginyu.cmd.Command;
import ginyu.common.Attributes;
import ginyu.core.Client;
import ginyu.db.Database;
import ginyu.exception.CommandValidateException;
import ginyu.object.ObjectType;
import ginyu.object.ZSetObject;
import ginyu.protocol.Arrays;
import ginyu.protocol.Integers;
import ginyu.protocol.Resp2;
import ginyu.protocol.Validates;
import ginyu.utils.ProtocolValueUtils;
import io.netty.channel.ChannelHandlerContext;

import java.util.Collection;

/**
 * @author: junjiexun
 * @date: 2020/10/16 2:54 下午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "zrange")
public class ZRange extends AbstractRedisCommand<ZRangeArg, Arrays> {

    @Override
    protected ZRangeArg createArg(Arrays arrays) {
        return new ZRangeArg(
                ProtocolValueUtils.getFromBulkStringsInArrays(arrays, 1),
                ProtocolValueUtils.getIntFromBulkStringsInArrays(arrays, 2),
                ProtocolValueUtils.getIntFromBulkStringsInArrays(arrays, 3),
                arrays.size() > 4
        );
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        Validates.validateArraysSize(commandName, arrays, 4, null);
        Validates.validateLong(arrays, 2, "start");
        Validates.validateLong(arrays, 3, "stop");
        if (arrays.size() > 4
                && !ProtocolValueUtils.getFromBulkStringsInArrays(arrays, 4).equalsIgnoreCase("withscores")) {
            throw new CommandValidateException("The option name is 'WITHSCORES'");
        }
    }

    @Override
    protected Resp2 doCommand0(ZRangeArg arg, ChannelHandlerContext ctx) {
        Client client = Attributes.getClient(ctx);
        Database database = client.getDatabase();
        ZSetObject zSetObject = Validates.validateType(database.get(arg.getKey()), ObjectType.ZSET);
        if (zSetObject == null) {
            return Arrays.EMPTY;
        }
        Integer start = arg.getStart();
        if (start < 0) {
            start = zSetObject.getOriginal().size() + start;
        }
        Integer stop = arg.getStop();
        if (stop < 0) {
            stop = zSetObject.getOriginal().size() + stop;
        }
        if (start > stop) {
            return Arrays.EMPTY;
        }
        Collection<String> data = zSetObject.getOriginal().getDataByIndexRange(start, stop, arg.getWithScores());
        if (data == null || data.isEmpty()) {
            return Arrays.EMPTY;
        }
        return Arrays.createByStringCollection(data);
    }
}
