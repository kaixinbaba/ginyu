package ginyu.cmd.connection;

import ginyu.cmd.AbstractRedisCommand;
import ginyu.cmd.Command;
import ginyu.core.Server;
import ginyu.protocol.*;
import ginyu.utils.ProtocolValueUtils;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author: junjiexun
 * @date: 2020/10/13 1:56 下午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "swapdb")
public class SwapDb extends AbstractRedisCommand<SwapDbArg, SimpleStrings> {
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
        Validates.validateArraysSize(commandName, arrays, 3);
        Validates.validateDbIndex(arrays, 1, "index1");
        Validates.validateDbIndex(arrays, 2, "index2");
    }

    @Override
    protected Resp2 doCommand0(SwapDbArg arg, ChannelHandlerContext ctx) {
        Server.INSTANCE.getDb().swapDb(arg.getIndex1(), arg.getIndex2());
        return SimpleStrings.OK;
    }
}
