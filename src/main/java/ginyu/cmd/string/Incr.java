package ginyu.cmd.string;

import ginyu.cmd.AbstractRedisCommand;
import ginyu.cmd.Command;
import ginyu.cmd.KeyArg;
import ginyu.common.Attributes;
import ginyu.core.Client;
import ginyu.db.Database;
import ginyu.exception.CommandValidateException;
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
@Command(name = "incr", checkExpire = true)
public class Incr extends AbstractRedisCommand<KeyArg, BulkStrings> {
    @Override
    public KeyArg createArg(Arrays arrays) {
        return new KeyArg(ProtocolValueUtils.getFromBulkStringsInArrays(arrays, 1));
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        Validates.validateArraysSize(commandName, arrays, 2);
    }

    @Override
    protected Resp2 doCommand0(KeyArg arg, ChannelHandlerContext ctx) {
        Client client = Attributes.getClient(ctx);
        Database database = client.getDatabase();
        StringObject stringObject = Validates.validateType(database.get(arg.getKey()), ObjectType.STRING);
        if (stringObject == null) {
            stringObject = new StringObject("1");
            database.set(arg.getKey(), stringObject);
            return BulkStrings.create(stringObject.getOriginal().getValue());
        } else {
            String value = stringObject.getOriginal().getValue();
            try {
                Integer intValue = Integer.parseInt(value);
                stringObject.getOriginal().setValue(intValue + 1);
                return BulkStrings.create(stringObject.getOriginal().getValue());
            } catch (NumberFormatException e) {
                throw new CommandValidateException("value is not an integer or out of range");
            }
        }
    }
}
