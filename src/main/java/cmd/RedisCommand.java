package cmd;

import core.Server;
import io.netty.channel.ChannelHandlerContext;
import protocol.Arrays;

public interface RedisCommand<T> {

    void doCommand(String commandName, Arrays arrays, ChannelHandlerContext ctx, Server server) throws Exception;

}
