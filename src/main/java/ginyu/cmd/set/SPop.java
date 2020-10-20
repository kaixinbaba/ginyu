package ginyu.cmd.set;

import ginyu.cmd.AbstractRedisCommand;
import ginyu.cmd.Command;
import ginyu.common.Attributes;
import ginyu.core.Client;
import ginyu.db.Database;
import ginyu.object.ObjectType;
import ginyu.object.SetObject;
import ginyu.protocol.Arrays;
import ginyu.protocol.BulkStrings;
import ginyu.protocol.Resp2;
import ginyu.protocol.Validates;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @author: junjiexun
 * @date: 2020/10/16 10:41 上午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "spop")
public class SPop extends AbstractRedisCommand<SPopArg, Arrays> {

    @Override
    protected SPopArg createArg(Arrays arrays) {
        List<String> argList = arrays.map2String(true);
        String key = argList.get(0);
        Integer count = 1;
        if (argList.size() > 1) {
            count = Integer.parseInt(argList.get(1));
        }
        return new SPopArg(key, count);
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        Validates.validateArraysSize(commandName, arrays, 2, null);
    }

    @Override
    protected Resp2 doCommand0(SPopArg arg, ChannelHandlerContext ctx) {
        Client client = Attributes.getClient(ctx);
        Database database = client.getDatabase();
        SetObject setObject = Validates.validateType(database.get(arg.getKey()), ObjectType.SET);
        if (setObject == null) {
            return BulkStrings.NULL;
        }
        int i = 0;
        Set<String> result = new HashSet<>();
        for (String member : setObject.getOriginal().keySet()) {
            if (i >= arg.getCount()) {
                break;
            }
            setObject.getOriginal().remove(member);
            result.add(member);
            i++;
        }
        database.deleteIfNeeded(arg.getKey());
        return Arrays.createByStringCollection(result);
    }
}
