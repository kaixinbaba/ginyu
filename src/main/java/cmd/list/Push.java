package cmd.list;

import cmd.AbstractRedisCommand;
import common.Attributes;
import common.Constants;
import core.Client;
import core.Server;
import db.Database;
import io.netty.channel.ChannelHandlerContext;
import object.ListObject;
import object.ObjectType;
import protocol.Arrays;
import protocol.Integers;
import protocol.Resp2;
import protocol.Validates;

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
        Database database = Server.INSTANCE.getDb().getDatabase(client.getDb());
        ListObject listObject = Validates.validateType(database.get(arg.getKey()), ObjectType.LIST);
        if (listObject == null) {
            listObject = new ListObject();
            database.set(arg.getKey(), listObject);
        }
        listObject.getOriginal().push(this.isLeft(), arg.getValues());
        return Integers.create(listObject.getOriginal().size());
    }

    protected abstract boolean isLeft();
}
