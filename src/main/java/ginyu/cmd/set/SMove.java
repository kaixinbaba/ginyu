package ginyu.cmd.set;

import ginyu.cmd.AbstractRedisCommand;
import ginyu.cmd.Command;
import ginyu.common.Attributes;
import ginyu.core.Client;
import ginyu.core.Server;
import ginyu.db.Database;
import ginyu.object.ObjectType;
import ginyu.object.SetObject;
import ginyu.protocol.Arrays;
import ginyu.protocol.Integers;
import ginyu.protocol.Resp2;
import ginyu.protocol.Validates;
import ginyu.utils.ProtocolValueUtils;
import io.netty.channel.ChannelHandlerContext;

import static ginyu.object.SetObject.NONE;

/**
 * @author: junjiexun
 * @date: 2020/10/13 1:56 下午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "smove")
public class SMove extends AbstractRedisCommand<SMoveArg, Integers> {
    @Override
    public SMoveArg createArg(Arrays arrays) {
        return new SMoveArg(
                ProtocolValueUtils.getFromBulkStringsInArrays(arrays, 1),
                ProtocolValueUtils.getFromBulkStringsInArrays(arrays, 2),
                ProtocolValueUtils.getFromBulkStringsInArrays(arrays, 3)
        );
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        Validates.validateArraysSize(commandName, arrays, 4);
    }

    @Override
    protected Resp2 doCommand0(SMoveArg arg, ChannelHandlerContext ctx) {
        Client client = Attributes.getClient(ctx);
        Database database = client.getDatabase();
        SetObject sourceObject = Validates.validateType(database.get(arg.getSource()), ObjectType.SET);
        if (sourceObject == null) {
            return Integers.ZERO;
        }
        SetObject destinationObject = Validates.validateType(database.get(arg.getDestination()), ObjectType.SET);
        SetObject.None remove = sourceObject.getOriginal().remove(arg.getMember());
        if (remove == null) {
            // 要移动的key不存在
            return Integers.ZERO;
        }
        if (sourceObject.getOriginal().isEmpty()) {
            database.delete(arg.getSource());
        }
        if (destinationObject == null) {
            destinationObject = new SetObject();
            destinationObject.getOriginal().put(arg.getMember(), NONE);
            database.set(arg.getDestination(), destinationObject);
        }
        return Integers.ONE;
    }
}
