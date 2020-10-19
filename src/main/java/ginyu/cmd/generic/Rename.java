package ginyu.cmd.generic;

import ginyu.cmd.AbstractRedisCommand;
import ginyu.cmd.Command;
import ginyu.common.Attributes;
import ginyu.core.Client;
import ginyu.core.Server;
import ginyu.db.Database;
import ginyu.exception.GinyuException;
import ginyu.object.RedisObject;
import ginyu.protocol.Arrays;
import ginyu.protocol.Resp2;
import ginyu.protocol.SimpleStrings;
import ginyu.protocol.Validates;
import ginyu.utils.ProtocolValueUtils;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author: junjiexun
 * @date: 2020/10/13 1:56 下午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "rename")
public class Rename extends AbstractRedisCommand<RenameArg, SimpleStrings> {
    @Override
    public RenameArg createArg(Arrays arrays) {
        return new RenameArg(
                ProtocolValueUtils.getFromBulkStringsInArrays(arrays, 1),
                ProtocolValueUtils.getFromBulkStringsInArrays(arrays, 2));
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        Validates.validateArraysSize(commandName, arrays, 3);
    }

    @Override
    protected Resp2 doCommand0(RenameArg arg, ChannelHandlerContext ctx) {
        Client client = Attributes.getClient(ctx);
        Database database = Server.INSTANCE.getDb().getDatabase(client.getDb());
        RedisObject value = database.remove(arg.getKey());
        if (value == null) {
            throw new GinyuException("no such key '%s'", arg.getKey());
        }
        database.set(arg.getNewKey(), value);
        Long expired = database.removeExpire(arg.getKey());
        if (expired != null) {
            database.setExpired(arg.getNewKey(), expired);
        }
        return SimpleStrings.OK;
    }
}
