package ginyu.cmd.set;

import ginyu.cmd.AbstractRedisCommand;
import ginyu.cmd.Command;
import ginyu.cmd.KeyArg;
import ginyu.common.Attributes;
import ginyu.core.Client;
import ginyu.core.Server;
import ginyu.db.Database;
import ginyu.object.ObjectType;
import ginyu.object.SetObject;
import ginyu.protocol.Arrays;
import ginyu.protocol.Resp2;
import ginyu.protocol.Validates;
import ginyu.utils.ProtocolValueUtils;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author: junjiexun
 * @date: 2020/10/13 1:56 下午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "smembers", checkExpire = true)
public class SMembers extends AbstractRedisCommand<KeyArg, Arrays> {
    @Override
    public KeyArg createArg(Arrays arrays) {
        return new KeyArg(ProtocolValueUtils.getFromBulkStringsInArrays(arrays, 1));
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        Validates.validateArraysSize(commandName, arrays, 2);
    }

    @Override
    protected Resp2 doCommand0(KeyArg arg, ChannelHandlerContext ctx) {
        Client client = Attributes.getClient(ctx);
        Database database = Server.INSTANCE.getDb().getDatabase(client.getDb());
        SetObject setObject = Validates.validateType(database.get(arg.getKey()), ObjectType.SET);
        if (setObject == null) {
            return Arrays.EMPTY;
        }
        return Arrays.createByStringCollection(setObject.getOriginal().keySet());
    }
}
