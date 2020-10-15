package cmd.generic;

import cmd.AbstractRedisCommand;
import cmd.Command;
import common.Attributes;
import core.Client;
import core.Server;
import db.Database;
import io.netty.channel.ChannelHandlerContext;
import object.RedisObject;
import protocol.*;

/**
 * @author: junjiexun
 * @date: 2020/10/13 1:56 下午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "ttl")
public class TTL extends AbstractRedisCommand<TTLArg> {
    @Override
    public TTLArg createArg(Arrays arrays) {
        BulkStrings bulkStrings = (BulkStrings) arrays.getData().get(1);
        return new TTLArg(bulkStrings.getData().getContent());
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        Validates.validateArraysSize(commandName, arrays, 2);
    }

    @Override
    protected Resp2 doCommand0(TTLArg arg, ChannelHandlerContext ctx) {
        Client client = Attributes.getClient(ctx);
        Database database = Server.INSTANCE.getDb().getDatabase(client.getDb());
        boolean expired = database.checkIfExpired(arg.getKey());
        if (expired) {
            database.delete(arg.getKey());
            return Integers.N_TWO;
        }
        RedisObject value = database.get(arg.getKey());
        if (value == null) {
            return Integers.N_TWO;
        }
        Long expiredTimestamp = database.getExpired(arg.getKey());
        if (expiredTimestamp == null) {
            return Integers.N_ONE;
        }
        return Integers.create((int) (expiredTimestamp - System.currentTimeMillis()) / 1000);
    }
}
