package ginyu.cmd.sortedset;

import ginyu.cmd.AbstractRedisCommand;
import ginyu.cmd.Command;
import ginyu.protocol.Arrays;
import ginyu.protocol.Integers;
import ginyu.protocol.Resp2;
import ginyu.protocol.Validates;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author: junjiexun
 * @date: 2020/10/16 2:54 下午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "zadd")
public class ZAdd extends AbstractRedisCommand<ZAddArg, Integers> {

    @Override
    protected ZAddArg createArg(Arrays arrays) {
        return null;
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        Validates.validateArraysSize(commandName, arrays, 4, null);
    }

    @Override
    protected Resp2 doCommand0(ZAddArg arg, ChannelHandlerContext ctx) {
        return null;
    }
}
