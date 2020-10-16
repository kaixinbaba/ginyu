package cmd.set;

import cmd.AbstractRedisCommand;
import cmd.Command;
import common.Attributes;
import core.Client;
import core.Server;
import db.Database;
import io.netty.channel.ChannelHandlerContext;
import object.ObjectType;
import object.SetObject;
import protocol.Arrays;
import protocol.Resp2;
import protocol.Validates;
import utils.ProtocolValueUtils;

/**
 * @author: junjiexun
 * @date: 2020/10/13 1:56 下午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "smembers")
public class SMembers extends AbstractRedisCommand<SMembersArg> {
    @Override
    public SMembersArg createArg(Arrays arrays) {
        return new SMembersArg(ProtocolValueUtils.getFromBulkStringsInArrays(arrays, 1));
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        Validates.validateArraysSize(commandName, arrays, 2);
    }

    @Override
    protected Resp2 doCommand0(SMembersArg arg, ChannelHandlerContext ctx) {
        Client client = Attributes.getClient(ctx);
        Database database = Server.INSTANCE.getDb().getDatabase(client.getDb());
        boolean expired = database.checkIfExpired(arg.getKey());
        if (expired) {
            database.delete(arg.getKey());
            return Arrays.EMPTY;
        }
        SetObject setObject = Validates.validateType(database.get(arg.getKey()), ObjectType.SET);
        if (setObject == null) {
            return Arrays.EMPTY;
        }
        return Arrays.createByStringCollection(setObject.getOriginal().keySet());
    }
}
