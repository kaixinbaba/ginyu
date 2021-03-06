package ginyu.io.handler;

import ginyu.cmd.RedisCommand;
import ginyu.cmd.RedisCommands;
import ginyu.exception.ProtocolException;
import ginyu.exception.UnknowCommandException;
import ginyu.protocol.Arrays;
import ginyu.protocol.BulkStrings;
import ginyu.protocol.Resp2;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author: junjiexun
 * @date: 2020/10/11 10:24 下午
 * @description:
 */
@SuppressWarnings("all")
@ChannelHandler.Sharable
public class RespHandler extends SimpleChannelInboundHandler<Resp2> {

    public static final RespHandler INSTANCE = new RespHandler();

    private RespHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Resp2 msg) throws Exception {
        if (!(msg instanceof Arrays)) {
            throw new ProtocolException("The client must send the protocol in ARRAYS format");
        }
        Arrays arrays = (Arrays) msg;
        Resp2 resp2 = arrays.getData().get(0);
        if (!(resp2 instanceof BulkStrings)) {
            throw new ProtocolException("The first argument must in BULKSTRINGS format");
        }
        String commandName = ((BulkStrings) resp2).getData().getContent().toLowerCase();
        RedisCommand redisCommand = RedisCommands.COMMAND_MAP.get(commandName);
        if (redisCommand == null) {
            throw new UnknowCommandException("unknown command '%s'", commandName);
        }
        redisCommand.doCommand(commandName, arrays, ctx);
    }
}
