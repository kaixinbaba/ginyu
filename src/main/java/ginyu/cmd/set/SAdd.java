package ginyu.cmd.set;

import ginyu.cmd.AbstractRedisCommand;
import ginyu.cmd.Command;
import ginyu.common.Attributes;
import ginyu.common.Constants;
import ginyu.core.Client;
import ginyu.db.Database;
import ginyu.object.ObjectType;
import ginyu.object.SetObject;
import ginyu.protocol.Arrays;
import ginyu.protocol.Integers;
import ginyu.protocol.Resp2;
import ginyu.protocol.Validates;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

import static ginyu.object.SetObject.NONE;


/**
 * @author: junjiexun
 * @date: 2020/10/16 10:41 上午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "sadd")
public class SAdd extends AbstractRedisCommand<SAddArg, Integers> {

    @Override
    protected SAddArg createArg(Arrays arrays) {
        List<String> argList = arrays.map2String(true);
        return new SAddArg(
                argList.remove(0),
                argList.toArray(Constants.STR_EMPTY_ARRAY)
        );
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        Validates.validateArraysSize(commandName, arrays, 3, null);
    }

    @Override
    protected Resp2 doCommand0(SAddArg arg, ChannelHandlerContext ctx) {
        Client client = Attributes.getClient(ctx);
        Database database = client.getDatabase();
        SetObject setObject = Validates.validateType(database.get(arg.getKey()), ObjectType.SET);
        if (setObject == null) {
            setObject = new SetObject();
            database.set(arg.getKey(), setObject);
        }
        int added = 0;
        for (String member : arg.getMembers()) {
            if (!setObject.getOriginal().containsKey(member)) {
                added++;
            }
            setObject.getOriginal().put(member, NONE);
        }
        return Integers.create(added);
    }
}
