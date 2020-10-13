package cmd;

import io.netty.channel.ChannelHandlerContext;
import protocol.Arrays;

public interface RedisCommand<T> {

    void doCommand(String commandName, Arrays arrays, ChannelHandlerContext ctx);

}
