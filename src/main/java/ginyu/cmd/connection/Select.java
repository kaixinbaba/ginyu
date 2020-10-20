package ginyu.cmd.connection;

import ginyu.cmd.AbstractRedisCommand;
import ginyu.cmd.Command;
import ginyu.common.Attributes;
import ginyu.core.Client;
import ginyu.protocol.*;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author: junjiexun
 * @date: 2020/10/13 1:56 下午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "select")
public class Select extends AbstractRedisCommand<SelectArg, SimpleStrings> {
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
        client.select(arg.getIndex());
        return SimpleStrings.OK;
    }
}
