package cmd.connection;

import cmd.AbstractRedisCommand;
import cmd.Command;
import common.Attributes;
import core.Client;
import core.Server;
import exception.CommandValidateException;
import io.netty.channel.ChannelHandlerContext;
import protocol.*;

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
        Validates.validateArraysSize(commandName, arrays, 2);
        Validates.validateDbIndex(arrays, 1, "index");
    }

    @Override
    protected Resp2 doCommand0(SelectArg arg, ChannelHandlerContext ctx) {
        Client client = Attributes.getClient(ctx);
        client.setDb(arg.getIndex());
        return SimpleStrings.OK;
    }
}
