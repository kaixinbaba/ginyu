package ginyu.cmd.hash;

import ginyu.cmd.AbstractRedisCommand;
import ginyu.cmd.Command;
import ginyu.common.Attributes;
import ginyu.core.Client;
import ginyu.db.Database;
import ginyu.object.HashObject;
import ginyu.object.ObjectType;
import ginyu.protocol.Arrays;
import ginyu.protocol.BulkStrings;
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
@Command(name = "hget", checkExpire = true)
public class HGet extends AbstractRedisCommand<HGetArg, BulkStrings> {
    @Override
    public HGetArg createArg(Arrays arrays) {
        return new HGetArg(
                ProtocolValueUtils.getFromBulkStringsInArrays(arrays, 1),
                ProtocolValueUtils.getFromBulkStringsInArrays(arrays, 2));
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        Validates.validateArraysSize(commandName, arrays, 3);
    }

    @Override
    protected Resp2 doCommand0(HGetArg arg, ChannelHandlerContext ctx) {
        Client client = Attributes.getClient(ctx);
        Database database = client.getDatabase();
        HashObject hashObject = Validates.validateType(database.get(arg.getKey()), ObjectType.HASH);
        if (hashObject == null) {
            return BulkStrings.NULL;
        }
        String value = hashObject.getOriginal().get(arg.getField());
        if (value == null) {
            return BulkStrings.NULL;
        }
        return BulkStrings.create(value);
    }
}
