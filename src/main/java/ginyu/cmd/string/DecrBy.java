package ginyu.cmd.string;

import ginyu.cmd.AbstractRedisCommand;
import ginyu.cmd.Command;
import ginyu.common.Attributes;
import ginyu.core.Client;
import ginyu.db.Database;
import ginyu.object.ObjectType;
import ginyu.object.StringObject;
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
@Command(name = "decrby", checkExpire = true)
public class DecrBy extends AbstractRedisCommand<CrementByArg, BulkStrings> {
    @Override
    public CrementByArg createArg(Arrays arrays) {
        return new CrementByArg(
                ProtocolValueUtils.getFromBulkStringsInArrays(arrays, 1),
                ProtocolValueUtils.getIntFromBulkStringsInArrays(arrays, 2)
        );
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        Validates.validateArraysSize(commandName, arrays, 3);
        Validates.validateInteger(arrays, 2, "decrement");
    }

    @Override
    protected Resp2 doCommand0(CrementByArg arg, ChannelHandlerContext ctx) {
        Client client = Attributes.getClient(ctx);
        Database database = client.getDatabase();
        StringObject stringObject = Validates.validateType(database.get(arg.getKey()), ObjectType.STRING);
        if (stringObject == null) {
            stringObject = new StringObject(-arg.getCrement());
            database.set(arg.getKey(), stringObject);
            return BulkStrings.create(stringObject.getOriginal().getValue());
        } else {
            stringObject.getOriginal().incrBy(-arg.getCrement());
            return BulkStrings.create(stringObject.getOriginal().getValue());
        }
    }
}
