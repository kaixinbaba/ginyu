package cmd.sortedset;

import cmd.AbstractRedisCommand;
import cmd.Command;
import io.netty.channel.ChannelHandlerContext;
import protocol.Arrays;
import protocol.Resp2;
import protocol.Validates;

/**
 * @author: junjiexun
 * @date: 2020/10/16 2:54 下午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "zadd")
public class ZAdd extends AbstractRedisCommand<ZAddArg> {

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
