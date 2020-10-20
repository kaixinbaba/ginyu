package ginyu.cmd.list;

import ginyu.cmd.AbstractRedisCommand;
import ginyu.cmd.Command;
import ginyu.common.Attributes;
import ginyu.core.Client;
import ginyu.core.Server;
import ginyu.db.Database;
import ginyu.object.ListObject;
import ginyu.object.ObjectType;
import ginyu.protocol.Arrays;
import ginyu.protocol.Resp2;
import ginyu.protocol.SimpleStrings;
import ginyu.protocol.Validates;
import ginyu.utils.ConvertUtils;
import ginyu.utils.ProtocolValueUtils;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author: junjiexun
 * @date: 2020/10/13 1:56 下午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "ltrim", checkExpire = true)
public class LTrim extends AbstractRedisCommand<LRangeArg, SimpleStrings> {
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
        Database database = client.getDatabase();
        ListObject listObject = Validates.validateType(database.get(arg.getKey()), ObjectType.LIST);
        if (listObject == null) {
            return SimpleStrings.OK;
        }
        final int size = listObject.getOriginal().size();
        int start = ConvertUtils.getCorrectIndex(arg.getStart(), size);
        int stop = ConvertUtils.getCorrectIndex(arg.getStop(), size);
        if (start > stop) {
            database.delete(arg.getKey());
            return SimpleStrings.OK;
        }
        for (int i = 0; i < start; i++) {
            listObject.getOriginal().pop(true);
        }
        for (int i = 0; i < size - 1 - stop; i++) {
            listObject.getOriginal().pop(false);
        }
        return SimpleStrings.OK;
    }
}
