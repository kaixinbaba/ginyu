package ginyu.cmd.list;

import ginyu.cmd.AbstractRedisCommand;
import ginyu.cmd.KeyArg;
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
import ginyu.utils.ProtocolValueUtils;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author: junjiexun
 * @date: 2020/10/13 1:56 下午
 * @description:
 */
@SuppressWarnings("all")
public abstract class Pop extends AbstractRedisCommand<KeyArg, BulkStrings> {
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
        Database database = Server.INSTANCE.getDb().getDatabase(client.getDb());
        ListObject listObject = Validates.validateType(database.get(arg.getKey()), ObjectType.LIST);
        if (listObject == null) {
            return BulkStrings.NULL;
        }
        String value = listObject.getOriginal().pop(this.isLeft());
        if (listObject.getOriginal().isEmpty()) {
            database.delete(arg.getKey());
        }
        return BulkStrings.create(value);
    }

    protected abstract boolean isLeft();
}
