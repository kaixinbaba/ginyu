package cmd.string;

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
import protocol.Resp2;

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
        if (arrays.getData().size() != 2) {
            throw new CommandValidateException("ERR wrong number of arguments for '%s' command", commandName);
        }
    }

    @Override
    protected Resp2 doCommand0(GetArg arg, ChannelHandlerContext ctx) {
        Client client = Attributes.getClient(ctx);
        System.out.println(client);
        Database database = Server.INSTANCE.getDb().getDatabase(client.getDb());
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
