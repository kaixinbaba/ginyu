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
import ginyu.protocol.Integers;
import ginyu.protocol.Resp2;
import ginyu.protocol.Validates;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

/**
 * @author: junjiexun
 * @date: 2020/10/16 10:41 上午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "hdel", checkExpire = true)
public class HDel extends AbstractRedisCommand<HDelArg, Integers> {
    @Override
    protected HDelArg createArg(Arrays arrays) {
        List<String> argList = arrays.map2String(true);
        return new HDelArg(
                argList.remove(0),
                argList.toArray(Constants.STR_EMPTY_ARRAY)
        );
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        Validates.validateArraysSize(commandName, arrays, 3, null);
    }

    @Override
    protected Resp2 doCommand0(HDelArg arg, ChannelHandlerContext ctx) {
        Client client = Attributes.getClient(ctx);
        Database database = Server.INSTANCE.getDb().getDatabase(client.getDb());
        HashObject hashObject = Validates.validateType(database.get(arg.getKey()), ObjectType.HASH);
        if (hashObject == null) {
            return Integers.ZERO;
        }
        int deleted = 0;
        for (String field : arg.getFields()) {
            String value = hashObject.getOriginal().remove(field);
            if (value != null) {
                deleted++;
            }
        }
        if (hashObject.getOriginal().isEmpty()) {
            database.delete(arg.getKey());
        }
        return Integers.create(deleted);
    }
}
