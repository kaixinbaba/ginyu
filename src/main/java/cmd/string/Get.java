package cmd.string;

import cmd.AbstractRedisCommand;
import cmd.Command;
import common.Attributes;
import core.Client;
import core.Server;
import db.Database;
import io.netty.channel.ChannelHandlerContext;
import object.StringObject;
import protocol.Arrays;
import protocol.BulkStrings;
import protocol.Resp2;
import protocol.Validates;

/**
 * @author: junjiexun
 * @date: 2020/10/13 1:56 下午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "get")
public class Get extends AbstractRedisCommand<GetArg> {
    @Override
    public GetArg createArg(Arrays arrays) {
        BulkStrings bulkStrings = (BulkStrings) arrays.getData().get(1);
        return new GetArg(bulkStrings.getData().getContent());
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        Validates.validateArraysSize(commandName, arrays, 2);
    }

    @Override
    protected Resp2 doCommand0(GetArg arg, ChannelHandlerContext ctx) {
        Client client = Attributes.getClient(ctx);
        Database database = Server.INSTANCE.getDb().getDatabase(client.getDb());
        boolean expired = database.checkIfExpired(arg.getKey());
        if (expired) {
            database.delete(arg.getKey());
            return BulkStrings.NULL;
        }
        StringObject stringObject = database.getString(arg.getKey());
        return object2Resp(stringObject);
    }

    private Resp2 object2Resp(StringObject stringObject) {
        if (stringObject == null) {
            return BulkStrings.NULL;
        } else {
            return BulkStrings.create(stringObject.getOriginal());
        }
    }
}
