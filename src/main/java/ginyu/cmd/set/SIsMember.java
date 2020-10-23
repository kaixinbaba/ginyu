package ginyu.cmd.set;

import ginyu.cmd.AbstractRedisCommand;
import ginyu.cmd.Command;
import ginyu.common.Attributes;
import ginyu.core.Client;
import ginyu.db.Database;
import ginyu.object.ObjectType;
import ginyu.object.SetObject;
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
@Command(name = "sismember", checkExpire = true)
public class SIsMember extends AbstractRedisCommand<SIsMemberArg, Integers> {
    @Override
    public SIsMemberArg createArg(Arrays arrays) {
        return new SIsMemberArg(
                ProtocolValueUtils.getFromBulkStringsInArrays(arrays, 1),
                ProtocolValueUtils.getFromBulkStringsInArrays(arrays, 2));
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        Validates.validateArraysSize(commandName, arrays, 3);
    }

    @Override
    protected Resp2 doCommand0(SIsMemberArg arg, ChannelHandlerContext ctx) {
        Client client = Attributes.getClient(ctx);
        Database database = client.getDatabase();
        SetObject setObject = Validates.validateType(database.get(arg.getKey()), ObjectType.SET);
        if (setObject == null) {
            return Integers.ZERO;
        }
        return setObject.getOriginal().containsKey(arg.getMember()) ? Integers.ONE : Integers.ZERO;
    }
}
