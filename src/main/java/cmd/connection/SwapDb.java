package cmd.connection;

import cmd.AbstractRedisCommand;
import cmd.Command;
import core.Server;
import exception.CommandValidateException;
import io.netty.channel.ChannelHandlerContext;
import protocol.*;
import utils.ProtocolValueUtils;

/**
 * @author: junjiexun
 * @date: 2020/10/13 1:56 下午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "swapdb")
public class SwapDb extends AbstractRedisCommand<SwapDbArg> {
    @Override
    public SwapDbArg createArg(Arrays arrays) {
        BulkStrings bulkStrings = (BulkStrings) arrays.getData().get(1);
        return new SwapDbArg(
                ProtocolValueUtils.getIntFromBulkStringsInArrays(arrays, 1),
                ProtocolValueUtils.getIntFromBulkStringsInArrays(arrays, 2)
        );
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        if (arrays.getData().size() != 3) {
            throw new CommandValidateException("wrong number of arguments for '%s' command", commandName);
        }
        Validates.validateDbIndex(arrays, 1, "index1");
        Validates.validateDbIndex(arrays, 2, "index2");
    }

    @Override
    protected Resp2 doCommand0(SwapDbArg arg, ChannelHandlerContext ctx) {
        Server.INSTANCE.getDb().swapDb(arg.getIndex1(), arg.getIndex2());
        return SimpleStrings.OK;
    }
}
