package cmd.connection;

import cmd.AbstractRedisCommand;
import cmd.Command;
import common.Attributes;
import core.Client;
import core.Server;
import exception.CommandValidateException;
import io.netty.channel.ChannelHandlerContext;
import protocol.Arrays;
import protocol.BulkStrings;
import protocol.Resp2;
import protocol.SimpleStrings;

/**
 * @author: junjiexun
 * @date: 2020/10/13 1:56 下午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "select")
public class Select extends AbstractRedisCommand<SelectArg> {
    @Override
    public SelectArg createArg(Arrays arrays) {
        BulkStrings bulkStrings = (BulkStrings) arrays.getData().get(1);
        return new SelectArg(Integer.valueOf(bulkStrings.getData().getContent()));
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        if (arrays.getData().size() != 2) {
            throw new CommandValidateException("wrong number of arguments for '%s' command", commandName);
        }
        Integer index;
        try {
            index = Integer.parseInt(((BulkStrings) arrays.getData().get(1)).getData().getContent());
        } catch (NumberFormatException e) {
            throw new CommandValidateException("index must be integer");
        }
        if (index < 0 || index >= Server.INSTANCE.getGinyuConfig().getDbSize()) {
            throw new CommandValidateException("DB index is out of range");
        }
    }

    @Override
    protected Resp2 doCommand0(SelectArg arg, ChannelHandlerContext ctx) {
        Client client = Attributes.getClient(ctx);
        client.setDb(arg.getIndex());
        return SimpleStrings.OK;
    }
}
