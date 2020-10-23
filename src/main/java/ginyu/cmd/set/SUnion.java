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
        Database database = client.getDatabase();
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
