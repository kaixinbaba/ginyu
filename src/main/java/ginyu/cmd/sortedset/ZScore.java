package ginyu.cmd.sortedset;

import ginyu.cmd.AbstractRedisCommand;
import ginyu.cmd.Command;
import ginyu.common.Attributes;
import ginyu.core.Client;
import ginyu.db.Database;
import ginyu.object.HashObject;
import ginyu.object.ObjectType;
import ginyu.object.ZSetObject;
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
@Command(name = "zscore", checkExpire = true)
public class ZScore extends AbstractRedisCommand<ZScoreArg, BulkStrings> {
    @Override
    public ZScoreArg createArg(Arrays arrays) {
        return new ZScoreArg(
                ProtocolValueUtils.getFromBulkStringsInArrays(arrays, 1),
                ProtocolValueUtils.getFromBulkStringsInArrays(arrays, 2));
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        Validates.validateArraysSize(commandName, arrays, 3);
    }

    @Override
    protected Resp2 doCommand0(ZScoreArg arg, ChannelHandlerContext ctx) {
        Client client = Attributes.getClient(ctx);
        Database database = client.getDatabase();
        ZSetObject zSetObject = Validates.validateType(database.get(arg.getKey()), ObjectType.ZSET);
        if (zSetObject == null) {
            return BulkStrings.NULL;
        }
        Double score = zSetObject.getOriginal().getScoreByMember(arg.getMember());
        if (score == null) {
            return BulkStrings.NULL;
        }
        return BulkStrings.create(score);
    }
}
