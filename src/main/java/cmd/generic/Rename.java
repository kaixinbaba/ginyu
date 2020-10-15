package cmd.generic;

import cmd.AbstractRedisCommand;
import cmd.Command;
import common.Attributes;
import core.Client;
import core.Server;
import db.Database;
import exception.GinyuException;
import io.netty.channel.ChannelHandlerContext;
import object.RedisObject;
import protocol.Arrays;
import protocol.Resp2;
import protocol.SimpleStrings;
import protocol.Validates;
import utils.ProtocolValueUtils;

/**
 * @author: junjiexun
 * @date: 2020/10/13 1:56 下午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "rename")
public class Rename extends AbstractRedisCommand<RenameArg> {
    @Override
    public RenameArg createArg(Arrays arrays) {
        return new RenameArg(
                ProtocolValueUtils.getFromBulkStringsInArrays(arrays, 1),
                ProtocolValueUtils.getFromBulkStringsInArrays(arrays, 2));
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        Validates.validateArraysSize(commandName, arrays, 3);
    }

    @Override
    protected Resp2 doCommand0(RenameArg arg, ChannelHandlerContext ctx) {
        Client client = Attributes.getClient(ctx);
        Database database = Server.INSTANCE.getDb().getDatabase(client.getDb());
        RedisObject value = database.remove(arg.getKey());
        if (value == null) {
            throw new GinyuException("no such key '%s'", arg.getKey());
        }
        database.set(arg.getNewKey(), value);
        Long expired = database.removeExpire(arg.getKey());
        if (expired != null) {
            database.setExpired(arg.getNewKey(), expired);
        }
        return SimpleStrings.OK;
    }
}
