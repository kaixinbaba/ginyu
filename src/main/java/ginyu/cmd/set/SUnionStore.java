package ginyu.cmd.set;

import ginyu.cmd.AbstractRedisCommand;
import ginyu.cmd.Command;
import ginyu.common.Attributes;
import ginyu.core.Client;
import ginyu.core.Server;
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
@Command(name = "sunionstore")
public class SUnionStore extends AbstractRedisCommand<SUnionStoreArg, Integers> {

    @Override
    public SUnionStoreArg createArg(Arrays arrays) {
        List<String> args = arrays.map2String(true);
        return new SUnionStoreArg(
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
    protected Resp2 doCommand0(SUnionStoreArg arg, ChannelHandlerContext ctx) {
        Client client = Attributes.getClient(ctx);
        Database database = client.getDatabase();
        Set<String> unionSet = new HashSet<>();
        for (int i = 0; i < arg.getKeys().length; i++) {
            SetObject setObject = Validates.validateType(database.get(arg.getKeys()[i]), ObjectType.SET);
            if (setObject != null) {
                unionSet.addAll(setObject.getOriginal().keySet());
            }
        }
        if (!unionSet.isEmpty()) {
            SetObject destination = new SetObject();
            destination.getOriginal().putAll(ConvertUtils.set2map(unionSet, NONE));
            RedisObject oldValue = database.set(arg.getDestination(), destination);
            if (oldValue != null) {
                // 有覆盖原来的key，所以要同时删除expired（不一定有，但是要检查下）
                database.cleanExpired(arg.getDestination());
            }
        }
        return Integers.create(unionSet.size());
    }
}
