package ginyu.cmd.list;

import ginyu.cmd.AbstractRedisCommand;
import ginyu.common.Attributes;
import ginyu.core.Client;
import ginyu.db.Database;
import ginyu.event.Events;
import ginyu.event.list.PushEvent;
import ginyu.object.ListObject;
import ginyu.object.ObjectType;
import ginyu.protocol.Arrays;
import ginyu.protocol.Integers;
import ginyu.protocol.Resp2;
import ginyu.protocol.Validates;
import ginyu.utils.ProtocolValueUtils;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;


/**
 * @author: junjiexun
 * @date: 2020/10/16 10:41 上午
 * @description:
 */
@SuppressWarnings("all")
public abstract class PushX extends AbstractRedisCommand<PushXArg, Integers> {

    @Override
    protected PushXArg createArg(Arrays arrays) {
        List<String> argList = arrays.map2String(true);
        return new PushXArg(
                ProtocolValueUtils.getFromBulkStringsInArrays(arrays, 1),
                ProtocolValueUtils.getFromBulkStringsInArrays(arrays, 2)
        );
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        Validates.validateArraysSize(commandName, arrays, 3);
    }

    @Override
    protected Resp2 doCommand0(PushXArg arg, ChannelHandlerContext ctx) {
        Client client = Attributes.getClient(ctx);
        Database database = client.getDatabase();
        ListObject listObject = Validates.validateType(database.get(arg.getKey()), ObjectType.LIST);
        if (listObject == null) {
            return Integers.ZERO;
        }
        listObject.getOriginal().push(this.isLeft(), arg.getValue());
        int size = listObject.getOriginal().size();
        Events.post(new PushEvent(client, arg.getKey(), size));
        return Integers.create(size);
    }

    protected abstract boolean isLeft();
}
