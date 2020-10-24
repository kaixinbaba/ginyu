package ginyu.cmd.sortedset;

import ginyu.cmd.AbstractRedisCommand;
import ginyu.cmd.Command;
import ginyu.common.Attributes;
import ginyu.core.Client;
import ginyu.db.Database;
import ginyu.object.ObjectType;
import ginyu.object.ZSetNode;
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
@Command(name = "zincrby")
public class ZIncrBy extends AbstractRedisCommand<ZIncrByArg, BulkStrings> {
    @Override
    public ZIncrByArg createArg(Arrays arrays) {
        return new ZIncrByArg(
                ProtocolValueUtils.getFromBulkStringsInArrays(arrays, 1),
                ProtocolValueUtils.getDoubleFromBulkStringsInArrays(arrays, 2),
                ProtocolValueUtils.getFromBulkStringsInArrays(arrays, 3)
        );
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        Validates.validateArraysSize(commandName, arrays, 4);
        Validates.validateDouble(arrays, 2, "increment");
    }

    @Override
    protected Resp2 doCommand0(ZIncrByArg arg, ChannelHandlerContext ctx) {
        Client client = Attributes.getClient(ctx);
        Database database = client.getDatabase();
        ZSetObject zSetObject = Validates.validateType(database.get(arg.getKey()), ObjectType.ZSET);
        if (zSetObject == null) {
            // 如果不存在 效果和 zadd 一致
            zSetObject = new ZSetObject();
            database.set(arg.getKey(), zSetObject);
            zSetObject.getOriginal().add(false, true, new ZSetNode(arg.getMember(), arg.getIncrement()));
            return BulkStrings.create(String.valueOf(arg.getIncrement()));
        }
        Double currentScore = zSetObject.getOriginal().getScoreByMember(arg.getMember());
        // 变成更新操作
        zSetObject.getOriginal().add(false, true, new ZSetNode(arg.getMember(), arg.getIncrement()));
        return BulkStrings.create(String.valueOf(currentScore + arg.getIncrement()));
    }
}
