package cmd.connection;

import cmd.AbstractRedisCommand;
import cmd.Command;
import exception.CommandValidateException;
import io.netty.channel.ChannelHandlerContext;
import protocol.Arrays;
import protocol.BulkStrings;
import protocol.Resp2;
import utils.ProtocolValueUtils;

/**
 * @author: junjiexun
 * @date: 2020/10/13 1:56 下午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "echo")
public class Echo extends AbstractRedisCommand<EchoArg> {
    @Override
    public EchoArg createArg(Arrays arrays) {
        return new EchoArg(ProtocolValueUtils.getFromBulkStringsInArrays(arrays, 1));
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        if (arrays.getData().size() != 2) {
            throw new CommandValidateException("wrong number of arguments for '%s' command", commandName);
        }
    }

    @Override
    protected Resp2 doCommand0(EchoArg arg, ChannelHandlerContext ctx) {
        return BulkStrings.create(arg.getMessage());
    }
}
