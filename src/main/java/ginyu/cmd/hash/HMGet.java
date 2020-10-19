package ginyu.cmd.hash;

import ginyu.cmd.AbstractRedisCommand;
import ginyu.cmd.Command;
import ginyu.common.Attributes;
import ginyu.common.Constants;
import ginyu.core.Client;
import ginyu.core.Server;
import ginyu.db.Database;
import ginyu.object.HashObject;
import ginyu.object.ObjectType;
import ginyu.protocol.Arrays;
import ginyu.protocol.Resp2;
import ginyu.protocol.Validates;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: junjiexun
 * @date: 2020/10/16 10:41 上午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "hmget", checkExpire = true)
public class HMGet extends AbstractRedisCommand<HMGetArg, Arrays> {
    @Override
    protected HMGetArg createArg(Arrays arrays) {
        List<String> argList = arrays.map2String(true);
        return new HMGetArg(
                argList.remove(0),
                argList.toArray(Constants.STR_EMPTY_ARRAY)
        );
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        Validates.validateArraysSize(commandName, arrays, 3, null);
    }

    @Override
    protected Resp2 getDefaultValue(Object arg) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return Arrays.createSpecifiedSizeWithNull(((HMGetArg) arg).getFields().length);
    }

    @Override
    protected Resp2 doCommand0(HMGetArg arg, ChannelHandlerContext ctx) {
        Client client = Attributes.getClient(ctx);
        Database database = Server.INSTANCE.getDb().getDatabase(client.getDb());
        HashObject hashObject = Validates.validateType(database.get(arg.getKey()), ObjectType.HASH);
        if (hashObject == null) {
            return Arrays.createSpecifiedSizeWithNull(arg.getFields().length);
        }
        List<String> data = new ArrayList<>(arg.getFields().length);
        for (String field : arg.getFields()) {
            data.add(hashObject.getOriginal().get(field));
        }
        return Arrays.createByStringCollection(data);
    }
}
