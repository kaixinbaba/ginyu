package cmd.generic;

import cmd.AbstractRedisCommand;
import cmd.Command;
import common.Attributes;
import core.Client;
import core.Server;
import db.Database;
import exception.CommandValidateException;
import io.netty.channel.ChannelHandlerContext;
import object.StringObject;
import protocol.Arrays;
import protocol.BulkStrings;
import protocol.Integers;
import protocol.Resp2;

import static common.Constants.STR_EMPTY_ARRAY;

/**
 * @author: junjiexun
 * @date: 2020/10/13 1:56 下午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "del")
public class Del extends AbstractRedisCommand<DelArg> {

    @Override
    public DelArg createArg(Arrays arrays) {
        return new DelArg(arrays.map2String().toArray(STR_EMPTY_ARRAY));
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        if (arrays.getData().size() < 2) {
            throw new CommandValidateException("ERR wrong number of arguments for '%s' command", commandName);
        }
    }

    @Override
    protected Resp2 doCommand0(DelArg arg, ChannelHandlerContext ctx) {
        Client client = Attributes.getClient(ctx);
        Database database = Server.INSTANCE.getDb().getDatabase(client.getDb());
        int deleted = database.delete(arg.getKeys());
        return Integers.create(deleted);
    }
}
