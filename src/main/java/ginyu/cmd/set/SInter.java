package ginyu.cmd.set;

import ginyu.cmd.AbstractRedisCommand;
import ginyu.cmd.Command;
import ginyu.common.Attributes;
import ginyu.core.Client;
import ginyu.db.Database;
import ginyu.object.ObjectType;
import ginyu.object.SetObject;
import ginyu.protocol.Arrays;
import ginyu.protocol.Resp2;
import ginyu.protocol.Validates;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashSet;
import java.util.Set;

import static ginyu.common.Constants.STR_EMPTY_ARRAY;

/**
 * @author: junjiexun
 * @date: 2020/10/13 1:56 下午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "sinter")
public class SInter extends AbstractRedisCommand<SInterArg, Arrays> {

    @Override
    public SInterArg createArg(Arrays arrays) {
        return new SInterArg(arrays.map2String(true).toArray(STR_EMPTY_ARRAY));
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        Validates.validateArraysSize(commandName, arrays, 2, null);
    }

    @Override
    protected Resp2 doCommand0(SInterArg arg, ChannelHandlerContext ctx) {
        Client client = Attributes.getClient(ctx);
        Database database = client.getDatabase();
        SetObject setObject = Validates.validateType(database.get(arg.getKeys()[0]), ObjectType.SET);
        if (setObject == null) {
            return Arrays.EMPTY;
        }
        Set<String> first = new HashSet<>(setObject.getOriginal().keySet());
        for (int i = 1; i < arg.getKeys().length; i++) {
            SetObject afterObject = Validates.validateType(database.get(arg.getKeys()[i]), ObjectType.SET);
            if (first.isEmpty() || afterObject == null || afterObject.getOriginal().isEmpty()) {
                // 只要一个key不存在 或者 空集 就直接返回空
                return Arrays.EMPTY;
            }
            first.retainAll(afterObject.getOriginal().keySet());
        }
        return Arrays.createByStringCollection(first);
    }
}
