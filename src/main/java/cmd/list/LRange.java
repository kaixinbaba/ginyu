package cmd.list;

import cmd.AbstractRedisCommand;
import cmd.Command;
import common.Attributes;
import core.Client;
import core.Server;
import db.Database;
import io.netty.channel.ChannelHandlerContext;
import object.ListObject;
import object.ObjectType;
import protocol.Arrays;
import protocol.Resp2;
import protocol.Validates;
import utils.ConvertUtils;
import utils.ProtocolValueUtils;

/**
 * @author: junjiexun
 * @date: 2020/10/13 1:56 下午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "lrange", checkExpire = true)
public class LRange extends AbstractRedisCommand<LRangeArg, Arrays> {
    @Override
    public LRangeArg createArg(Arrays arrays) {
        return new LRangeArg(
                ProtocolValueUtils.getFromBulkStringsInArrays(arrays, 1),
                ProtocolValueUtils.getIntFromBulkStringsInArrays(arrays, 2),
                ProtocolValueUtils.getIntFromBulkStringsInArrays(arrays, 3));
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        Validates.validateArraysSize(commandName, arrays, 4);
        Validates.validateInteger(arrays, 2, "start");
        Validates.validateInteger(arrays, 3, "stop");
    }

    @Override
    protected Resp2 doCommand0(LRangeArg arg, ChannelHandlerContext ctx) {
        Client client = Attributes.getClient(ctx);
        Database database = Server.INSTANCE.getDb().getDatabase(client.getDb());
        ListObject listObject = Validates.validateType(database.get(arg.getKey()), ObjectType.LIST);
        if (listObject == null) {
            return Arrays.EMPTY;
        }
        int size = listObject.getOriginal().size();
        int start = ConvertUtils.getPositiveIndex(arg.getStart(), size);
        int stop = ConvertUtils.getPositiveIndex(arg.getStop(), size);
        if (start > stop) {
            return Arrays.EMPTY;
        }
        return Arrays.createByStringCollection(listObject.getOriginal().range(start, stop));
    }
}
