package ginyu.cmd.list;

import ginyu.cmd.AbstractRedisCommand;
import ginyu.cmd.Command;
import ginyu.common.Attributes;
import ginyu.core.Client;
import ginyu.core.Server;
import ginyu.db.Database;
import ginyu.object.ListObject;
import ginyu.object.ObjectType;
import ginyu.protocol.Arrays;
import ginyu.protocol.BulkStrings;
import ginyu.protocol.Resp2;
import ginyu.protocol.Validates;
import ginyu.utils.ConvertUtils;
import ginyu.utils.ProtocolValueUtils;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author: junjiexun
 * @date: 2020/10/13 1:56 下午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "lindex", checkExpire = true)
public class LIndex extends AbstractRedisCommand<LIndexArg, BulkStrings> {
    @Override
    public LIndexArg createArg(Arrays arrays) {
        return new LIndexArg(
                ProtocolValueUtils.getFromBulkStringsInArrays(arrays, 1),
                ProtocolValueUtils.getIntFromBulkStringsInArrays(arrays, 2));
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        Validates.validateArraysSize(commandName, arrays, 3);
    }

    @Override
    protected Resp2 doCommand0(LIndexArg arg, ChannelHandlerContext ctx) {
        Client client = Attributes.getClient(ctx);
        Database database = Server.INSTANCE.getDb().getDatabase(client.getDb());
        ListObject listObject = Validates.validateType(database.get(arg.getKey()), ObjectType.LIST);
        if (listObject == null) {
            return BulkStrings.NULL;
        }
        final int size = listObject.getOriginal().size();
        int index = ConvertUtils.getPositiveIndex(arg.getIndex(), size);
        if (index < 0 || index >= size) {
            return BulkStrings.NULL;
        }
        String value = listObject.getOriginal().get(index);
        if (value == null) {
            return BulkStrings.NULL;
        }
        return BulkStrings.create(value);
    }
}
