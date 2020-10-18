package cmd.set;

import cmd.AbstractRedisCommand;
import cmd.Command;
import common.Attributes;
import core.Client;
import core.Server;
import db.Database;
import io.netty.channel.ChannelHandlerContext;
import object.ObjectType;
import object.SetObject;
import protocol.Arrays;
import protocol.Resp2;
import protocol.Validates;

import java.util.HashSet;
import java.util.Set;

import static common.Constants.STR_EMPTY_ARRAY;

/**
 * @author: junjiexun
 * @date: 2020/10/13 1:56 下午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "sunion")
public class SUnion extends AbstractRedisCommand<SUnionArg, Arrays> {

    @Override
    public SUnionArg createArg(Arrays arrays) {
        return new SUnionArg(arrays.map2String(true).toArray(STR_EMPTY_ARRAY));
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        Validates.validateArraysSize(commandName, arrays, 2, null);
    }

    @Override
    protected Resp2 doCommand0(SUnionArg arg, ChannelHandlerContext ctx) {
        Client client = Attributes.getClient(ctx);
        Database database = Server.INSTANCE.getDb().getDatabase(client.getDb());
        Set<String> unionSet = new HashSet<>();
        for (int i = 0; i < arg.getKeys().length; i++) {
            SetObject setObject = Validates.validateType(database.get(arg.getKeys()[i]), ObjectType.SET);
            if (setObject != null) {
                unionSet.addAll(setObject.getOriginal().keySet());
            }
        }
        return Arrays.createByStringCollection(unionSet);
    }
}
