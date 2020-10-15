package cmd.generic;

import cmd.AbstractRedisCommand;
import cmd.Command;
import common.Attributes;
import core.Client;
import core.Server;
import db.Database;
import io.netty.channel.ChannelHandlerContext;
import protocol.*;

/**
 * @author: junjiexun
 * @date: 2020/10/13 1:56 下午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "persist")
public class Persist extends AbstractRedisCommand<PersistArg> {
    @Override
    public PersistArg createArg(Arrays arrays) {
        BulkStrings bulkStrings = (BulkStrings) arrays.getData().get(1);
        return new PersistArg(bulkStrings.getData().getContent());
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        Validates.validateArraysSize(commandName, arrays, 2);
    }

    @Override
    protected Resp2 doCommand0(PersistArg arg, ChannelHandlerContext ctx) {
        Client client = Attributes.getClient(ctx);
        Database database = Server.INSTANCE.getDb().getDatabase(client.getDb());
        Integer persisted = database.cleanExpired(arg.getKey());
        return Integers.create(persisted);
    }
}
