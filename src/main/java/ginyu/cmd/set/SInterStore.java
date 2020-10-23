package ginyu.cmd.set;

import ginyu.cmd.AbstractRedisCommand;
import ginyu.cmd.Command;
import ginyu.common.Attributes;
import ginyu.core.Client;
import ginyu.db.Database;
import ginyu.object.ObjectType;
import ginyu.object.RedisObject;
import ginyu.object.SetObject;
import ginyu.protocol.Arrays;
import ginyu.protocol.Integers;
import ginyu.protocol.Resp2;
import ginyu.protocol.Validates;
import ginyu.utils.ConvertUtils;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ginyu.common.Constants.STR_EMPTY_ARRAY;
import static ginyu.object.SetObject.NONE;

/**
 * @author: junjiexun
 * @date: 2020/10/13 1:56 下午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "sinterstore")
public class SInterStore extends AbstractRedisCommand<SInterStoreArg, Integers> {

    @Override
    public SInterStoreArg createArg(Arrays arrays) {
        List<String> args = arrays.map2String(true);
        return new SInterStoreArg(
                // 上面的转换已经删除了命令本身，所以第一个字符串就是destination
                args.remove(0),
                args.toArray(STR_EMPTY_ARRAY)
        );
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        Validates.validateArraysSize(commandName, arrays, 3, null);
    }

    @Override
    protected Resp2 doCommand0(SInterStoreArg arg, ChannelHandlerContext ctx) {
        Client client = Attributes.getClient(ctx);
        Database database = client.getDatabase();
        SetObject setObject = Validates.validateType(database.get(arg.getKeys()[0]), ObjectType.SET);
        if (setObject == null) {
            return Integers.ZERO;
        }
        Set<String> first = new HashSet<>(setObject.getOriginal().keySet());
        for (int i = 1; i < arg.getKeys().length; i++) {
            SetObject afterObject = Validates.validateType(database.get(arg.getKeys()[i]), ObjectType.SET);
            if (first.isEmpty() || afterObject == null || afterObject.getOriginal().isEmpty()) {
                // 只要一个key不存在 或者 空集 就直接返回空
                return Integers.ZERO;
            }
            first.retainAll(afterObject.getOriginal().keySet());
        }
        if (!first.isEmpty()) {
            SetObject destination = new SetObject();
            destination.getOriginal().putAll(ConvertUtils.set2map(first, NONE));
            RedisObject oldValue = database.set(arg.getDestination(), destination);
            if (oldValue != null) {
                // 有覆盖原来的key，所以要同时删除expired（不一定有，但是要检查下）
                database.cleanExpired(arg.getDestination());
            }
        }
        return Integers.create(first.size());
    }
}
