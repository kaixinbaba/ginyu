package cmd;

import common.ThreadPools;
import exception.CommandValidateException;
import exception.GinyuException;
import io.netty.channel.ChannelHandlerContext;
import protocol.Arrays;
import protocol.Errors;
import protocol.Resp2;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author: junjiexun
 * @date: 2020/10/13 2:33 下午
 * @description:
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractRedisCommand<T> implements RedisCommand<T> {

    @Override
    public void doCommand(String commandName, Arrays arrays, ChannelHandlerContext ctx) {
        // TODO 命令执行的生命周期
        ThreadPools.POOL.execute(() -> {
            try {
                AbstractRedisCommand.this.validate(commandName, arrays);
                T arg = AbstractRedisCommand.this.createArg(arrays);
                Resp2 resp2 = AbstractRedisCommand.this.doCommand0(arg);
                ctx.writeAndFlush(resp2);
            } catch (GinyuException g) {
                throw g;
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        });
    }

    protected abstract T createArg(Arrays arrays);

    protected abstract void validate(String commandName, Arrays arrays);

    protected abstract Resp2 doCommand0(T arg);
}
