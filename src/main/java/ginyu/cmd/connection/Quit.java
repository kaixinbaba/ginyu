package ginyu.cmd.connection;

import ginyu.cmd.AbstractRedisCommand;
import ginyu.cmd.Command;
import ginyu.protocol.Arrays;
import ginyu.protocol.BulkStrings;
import ginyu.protocol.Resp2;
import ginyu.protocol.SimpleStrings;
import io.netty.channel.ChannelHandlerContext;

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
