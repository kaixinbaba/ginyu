package ginyu.cmd.connection;

import ginyu.cmd.AbstractRedisCommand;
import ginyu.cmd.Command;
import ginyu.protocol.*;
import io.netty.channel.ChannelHandlerContext;

import static ginyu.common.Constants.SLOGAN;

/**
 * @author: junjiexun
 * @date: 2020/10/13 1:56 下午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "ping")
public class Ping extends AbstractRedisCommand<PingArg, BulkStrings> {
    @Override
    public PingArg createArg(Arrays arrays) {
        if (arrays.getData().size() == 1) {
            return new PingArg(null);
        } else {
            return new PingArg(((BulkStrings) arrays.getData().get(1)).getData().getContent());
        }
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        Validates.validateArraysSize(commandName, arrays, null, 2);
    }

    @Override
    protected Resp2 doCommand0(PingArg arg, ChannelHandlerContext ctx) {
        if (arg.getMessage() == null) {
            return SimpleStrings.create(SLOGAN + "Ginyu!");
        } else {
            return BulkStrings.create(arg.getMessage());
        }
    }
}
