package cmd.connection;

import cmd.AbstractRedisCommand;
import cmd.Command;
import common.Attributes;
import core.Client;
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
@Command(name = "ping")
public class Ping extends AbstractRedisCommand<PingArg> {
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
        if (arrays.getData().size() > 2) {
            throw new CommandValidateException("wrong number of arguments for '%s' command", commandName);
        }
    }

    @Override
    protected Resp2 doCommand0(PingArg arg, ChannelHandlerContext ctx) {
        if (arg.getMessage() == null) {
            return SimpleStrings.create("ギニュー特戦队! Ginyu!");
        } else {
            return BulkStrings.create(arg.getMessage());
        }
    }
}
