package cmd.list;

import cmd.AbstractRedisCommand;
import common.Attributes;
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
import utils.ProtocolValueUtils;

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
        Database database = Server.INSTANCE.getDb().getDatabase(client.getDb());
        ListObject listObject = Validates.validateType(database.get(arg.getKey()), ObjectType.LIST);
        if (listObject == null) {
            return Integers.ZERO;
        }
        listObject.getOriginal().push(this.isLeft(), arg.getValue());
        return Integers.create(listObject.getOriginal().size());
    }

    protected abstract boolean isLeft();
}
