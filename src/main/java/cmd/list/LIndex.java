package cmd.list;

import cmd.AbstractRedisCommand;
import cmd.Command;
import common.Attributes;
import core.Client;
import core.Server;
import db.Database;
import io.netty.channel.ChannelHandlerContext;
import object.ListObject;
import object.ObjectType;
import protocol.Arrays;
import protocol.BulkStrings;
import protocol.Resp2;
import protocol.Validates;
import utils.ProtocolValueUtils;

import java.util.concurrent.LinkedBlockingDeque;

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
        int index = arg.getIndex();
        final int size = listObject.getOriginal().size();
        if (index < 0) {
            // lindex支持负数
            index = size + index;
        }
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
