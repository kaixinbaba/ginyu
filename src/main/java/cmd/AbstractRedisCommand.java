package cmd;

import common.Attributes;
import core.Client;
import core.Server;
import db.Database;
import exception.GinyuException;
import io.netty.channel.ChannelHandlerContext;
import protocol.Arrays;
import protocol.Resp2;
import utils.ReflectUtils;

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
                    Class respClass = ReflectUtils.getGenericTypeOnSuperClass(this, 1);
                    Method defaultValue = respClass.getDeclaredMethod("defaultValue");
                    ctx.writeAndFlush((Resp2) defaultValue.invoke(null));
                    return;
                }
            }
            Resp2 resp2 = AbstractRedisCommand.this.doCommand0(arg, ctx);
            ctx.writeAndFlush(resp2);
        } catch (GinyuException g) {
            throw g;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    protected abstract T createArg(Arrays arrays);

    protected void validate(String commandName, Arrays arrays) {
    }

    protected abstract Resp2 doCommand0(T arg, ChannelHandlerContext ctx);
}
