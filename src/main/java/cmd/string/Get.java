package cmd.string;

import cmd.AbstractRedisCommand;
import cmd.Command;
import cmd.KeyArg;
import common.Attributes;
import core.Client;
import core.Server;
import db.Database;
import io.netty.channel.ChannelHandlerContext;
import object.ObjectType;
import object.StringObject;
import protocol.Arrays;
import protocol.BulkStrings;
import protocol.Resp2;
import protocol.Validates;
import utils.ProtocolValueUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: junjiexun
 * @date: 2020/10/13 1:56 下午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "get", checkExpire = true)
public class Get extends AbstractRedisCommand<KeyArg, BulkStrings> {
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
        StringObject stringObject = Validates.validateType(database.get(arg.getKey()), ObjectType.STRING);
        if (stringObject == null) {
            return BulkStrings.NULL;
        }
        return BulkStrings.create(stringObject.getOriginal());
    }
}
