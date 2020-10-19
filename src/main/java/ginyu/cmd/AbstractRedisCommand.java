package ginyu.cmd;

import ginyu.common.Attributes;
import ginyu.core.Client;
import ginyu.core.Server;
import ginyu.db.Database;
import ginyu.exception.GinyuException;
import ginyu.protocol.Arrays;
import ginyu.protocol.Resp2;
import ginyu.utils.ReflectUtils;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author: junjiexun
 * @date: 2020/10/13 2:33 下午
 * @description:
 */
@SuppressWarnings("all")
public abstract class AbstractRedisCommand<T, R extends Resp2> implements RedisCommand {

    @Override
    public void doCommand(String commandName, Arrays arrays, ChannelHandlerContext ctx) throws Exception {
        // TODO 命令执行的生命周期, 异步？
        try {
            AbstractRedisCommand.this.validate(commandName, arrays);
            T arg = AbstractRedisCommand.this.createArg(arrays);
            Command commandAnno = this.getClass().getAnnotation(Command.class);
            if (commandAnno.checkExpire() && arg instanceof KeyArg) {
                KeyArg keyArg = (KeyArg) arg;
                Client client = Attributes.getClient(ctx);
                Database database = Server.INSTANCE.getDb().getDatabase(client.getDb());
                boolean expired = database.checkIfExpired(keyArg.getKey());
                if (expired) {
                    database.delete(keyArg.getKey());
                    ctx.writeAndFlush(this.getDefaultValue(arg));
                    return;
                }
            }
            Resp2 resp2 = AbstractRedisCommand.this.doCommand0(arg, ctx);
            // 若resp2为null，则会有异步的方式去响应客户端
            if (resp2 != null) {
                ctx.writeAndFlush(resp2);
            }
        } catch (GinyuException g) {
            throw g;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    protected Resp2 getDefaultValue(Object arg) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class respClass = ReflectUtils.getGenericTypeOnSuperClass(this, 1);
        Method defaultValue = respClass.getDeclaredMethod("defaultValue");
        return (Resp2) defaultValue.invoke(null);
    }

    protected abstract T createArg(Arrays arrays);

    protected void validate(String commandName, Arrays arrays) {
    }

    protected abstract Resp2 doCommand0(T arg, ChannelHandlerContext ctx);
}
