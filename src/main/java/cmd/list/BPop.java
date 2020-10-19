package cmd.list;

import cmd.AbstractRedisCommand;
import common.Attributes;
import core.Client;
import core.Server;
import db.Database;
import io.netty.channel.ChannelHandlerContext;
import object.ListObject;
import object.ObjectType;
import protocol.Arrays;
import protocol.Resp2;
import protocol.Validates;

import java.util.List;

import static common.Constants.STR_EMPTY_ARRAY;

/**
 * @author: junjiexun
 * @date: 2020/10/19 4:15 下午
 * @description:
 */
@SuppressWarnings("all")
public abstract class BPop extends AbstractRedisCommand<BPopArg, Arrays> {
    @Override
    protected BPopArg createArg(Arrays arrays) {
        List<String> args = arrays.map2String(true);
        // 最后一个参数是timeout
        Integer timeout = Integer.parseInt(args.remove(args.size() - 1));
        return new BPopArg(args.toArray(STR_EMPTY_ARRAY), timeout);
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        Validates.validateArraysSize(commandName, arrays, 3, null);
        Validates.validateInteger(arrays, arrays.size() - 1, "timeout");
    }

    @Override
    protected Resp2 doCommand0(BPopArg arg, ChannelHandlerContext ctx) {
        Client client = Attributes.getClient(ctx);
        Database database = Server.INSTANCE.getDb().getDatabase(client.getDb());
        for (String key : arg.getKeys()) {
            ListObject listObject = Validates.validateType(database.get(key), ObjectType.LIST);
            if (listObject != null && !listObject.getOriginal().isEmpty()) {
                String pop = listObject.getOriginal().pop(this.isLeft());
                return Arrays.createByStringArray(key, pop);
            }
        }
        // 执行到这里说明上面的key都不存在，需要进行订阅
        return null;
    }

    protected abstract boolean isLeft();
}
