package ginyu.cmd.list;

import ginyu.cmd.AbstractRedisCommand;
import ginyu.common.Attributes;
import ginyu.core.Client;
import ginyu.db.Database;
import ginyu.event.Events;
import ginyu.event.list.BlockByBPopEvent;
import ginyu.object.ListObject;
import ginyu.object.ObjectType;
import ginyu.protocol.Arrays;
import ginyu.protocol.Resp2;
import ginyu.protocol.Validates;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

import static ginyu.common.Constants.STR_EMPTY_ARRAY;

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
        Database database = client.getDatabase();
        for (String key : arg.getKeys()) {
            ListObject listObject = Validates.validateType(database.get(key), ObjectType.LIST);
            if (listObject != null && !listObject.getOriginal().isEmpty()) {
                String pop = listObject.getOriginal().pop(this.isLeft());
                database.deleteIfNeeded(key);
                return Arrays.createByStringArray(key, pop);
            }
        }
        // 执行到这里说明上面的key都不存在，需要进行订阅
        Events.post(new BlockByBPopEvent(client, arg, this.isLeft()));
        return null;
    }

    protected abstract boolean isLeft();
}
