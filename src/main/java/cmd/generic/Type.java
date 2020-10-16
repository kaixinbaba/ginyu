package cmd.generic;

import cmd.AbstractRedisCommand;
import cmd.Command;
import common.Attributes;
import core.Client;
import core.Server;
import db.Database;
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
@Command(name = "type")
public class Type extends AbstractRedisCommand<TypeArg> {
    @Override
    public TypeArg createArg(Arrays arrays) {
        return new TypeArg(ProtocolValueUtils.getFromBulkStringsInArrays(arrays, 1));
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        Validates.validateArraysSize(commandName, arrays, 2);
    }

    @Override
    protected Resp2 doCommand0(TypeArg arg, ChannelHandlerContext ctx) {
        Client client = Attributes.getClient(ctx);
        Database database = Server.INSTANCE.getDb().getDatabase(client.getDb());
        RedisObject redisObject = database.get(arg.getKey());
        if (redisObject == null) {
            return SimpleStrings.NONE;
        }
        return SimpleStrings.create(redisObject.getType().getDisplay());
    }
}
