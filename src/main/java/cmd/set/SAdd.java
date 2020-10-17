package cmd.set;

import cmd.AbstractRedisCommand;
import cmd.Command;
import common.Attributes;
import common.Constants;
import core.Client;
import core.Server;
import db.Database;
import io.netty.channel.ChannelHandlerContext;
import object.ObjectType;
import object.SetObject;
import protocol.Arrays;
import protocol.Integers;
import protocol.Resp2;
import protocol.Validates;

import java.util.List;

import static object.SetObject.NONE;


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
        Database database = Server.INSTANCE.getDb().getDatabase(client.getDb());
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
