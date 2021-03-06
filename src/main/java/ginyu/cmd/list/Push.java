package ginyu.cmd.list;

import ginyu.cmd.AbstractRedisCommand;
import ginyu.common.Attributes;
import ginyu.common.Constants;
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
import io.netty.channel.ChannelHandlerContext;

import java.util.List;


/**
 * @author: junjiexun
 * @date: 2020/10/16 10:41 上午
 * @description:
 */
@SuppressWarnings("all")
public abstract class Push extends AbstractRedisCommand<PushArg, Integers> {

    @Override
    protected PushArg createArg(Arrays arrays) {
        List<String> argList = arrays.map2String(true);
        return new PushArg(
                argList.remove(0),
                argList.toArray(Constants.STR_EMPTY_ARRAY)
        );
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        Validates.validateArraysSize(commandName, arrays, 3, null);
    }

    @Override
    protected Resp2 doCommand0(PushArg arg, ChannelHandlerContext ctx) {
        Client client = Attributes.getClient(ctx);
        Database database = client.getDatabase();
        ListObject listObject = Validates.validateType(database.get(arg.getKey()), ObjectType.LIST);
        if (listObject == null) {
            listObject = new ListObject();
            database.set(arg.getKey(), listObject);
        }
        listObject.getOriginal().push(this.isLeft(), arg.getValues());
        int size = listObject.getOriginal().size();
        Events.post(new PushEvent(client, arg.getKey(), size));
        return Integers.create(size);
    }

    protected abstract boolean isLeft();
}
