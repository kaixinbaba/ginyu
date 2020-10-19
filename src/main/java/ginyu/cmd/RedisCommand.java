package ginyu.cmd;

import ginyu.protocol.Arrays;
import io.netty.channel.ChannelHandlerContext;

public interface RedisCommand {

    void doCommand(String commandName, Arrays arrays, ChannelHandlerContext ctx) throws Exception;

}
