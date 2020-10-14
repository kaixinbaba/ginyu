package cmd;

import core.Server;
import exception.GinyuException;
import io.netty.channel.ChannelHandlerContext;
import object.RedisObject;
import protocol.Arrays;
import protocol.Resp2;

/**
 * @author: junjiexun
 * @date: 2020/10/13 2:33 下午
 * @description:
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractRedisCommand<T> implements RedisCommand<T> {

    @Override
    public void doCommand(String commandName, Arrays arrays, ChannelHandlerContext ctx, Server server) throws Exception {
        // TODO 命令执行的生命周期, 异步？
        try {
            AbstractRedisCommand.this.validate(commandName, arrays);
            T arg = AbstractRedisCommand.this.createArg(arrays);
            Resp2 resp2 = AbstractRedisCommand.this.doCommand0(arg, ctx);
            ctx.writeAndFlush(resp2);
        } catch (GinyuException g) {
            throw g;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    protected abstract T createArg(Arrays arrays);

    protected abstract void validate(String commandName, Arrays arrays);

    protected abstract Resp2 doCommand0(T arg, ChannelHandlerContext ctx);
}
