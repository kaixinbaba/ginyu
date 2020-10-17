package cmd.connection;

import cmd.AbstractRedisCommand;
import cmd.Command;
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
@Command(name = "quit")
public class Quit extends AbstractRedisCommand<Void, BulkStrings> {

    @Override
    public Void createArg(Arrays arrays) {
        return null;
    }

    @Override
    protected Resp2 doCommand0(Void arg, ChannelHandlerContext ctx) {
        ctx.channel().close();
        return SimpleStrings.OK;
    }
}
