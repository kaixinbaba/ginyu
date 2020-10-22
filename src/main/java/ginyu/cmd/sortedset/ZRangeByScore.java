package ginyu.cmd.sortedset;

import ginyu.cmd.AbstractRedisCommand;
import ginyu.cmd.Command;
import ginyu.common.Attributes;
import ginyu.core.Client;
import ginyu.db.Database;
import ginyu.exception.CommandValidateException;
import ginyu.object.ObjectType;
import ginyu.object.ZSetObject;
import ginyu.protocol.Arrays;
import ginyu.protocol.Resp2;
import ginyu.protocol.Validates;
import io.netty.channel.ChannelHandlerContext;

import java.util.Collection;
import java.util.List;

/**
 * @author: junjiexun
 * @date: 2020/10/16 2:54 下午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "zrangebyscore")
public class ZRangeByScore extends AbstractRedisCommand<ZRangeByScoreArg, Arrays> {

    @Override
    protected ZRangeByScoreArg createArg(Arrays arrays) {
        ZScoreRangeArg zScoreRangeArg = ZScoreRangeArg.create(arrays);

        List<String> args = arrays.map2String(true);
        Boolean withScores = false;
        Boolean limit = false;
        Integer offset = null;
        Integer count = null;
        for (int i = 3; i < args.size(); i++) {
            String arg = args.get(i);
            if (arg.equalsIgnoreCase("WITHSCORES")) {
                withScores = true;
            } else if (arg.equalsIgnoreCase("LIMIT")) {
                limit = true;
                try {
                    offset = Integer.parseInt(args.get(i + 1));
                    if (offset < 0) {
                        throw new CommandValidateException("'offset' must >= 0");
                    }
                    count = Integer.parseInt(args.get(i + 2));
                    if (count < 0) {
                        throw new CommandValidateException("'count' must >= 0");
                    }
                } catch (IndexOutOfBoundsException ie) {
                    throw new CommandValidateException("The 'offset' and 'count' are required");
                } catch (NumberFormatException ne) {
                    throw new CommandValidateException("The 'offset' and 'count' are both integer");
                }
            }
        }

        ZRangeByScoreArg zRangeByScoreArg = new ZRangeByScoreArg();
        zRangeByScoreArg.setKey(zScoreRangeArg.getKey());
        zRangeByScoreArg.setOpenIntervalMin(zScoreRangeArg.getOpenIntervalMin());
        zRangeByScoreArg.setMin(zScoreRangeArg.getMin());
        zRangeByScoreArg.setOpenIntervalMax(zScoreRangeArg.getOpenIntervalMax());
        zRangeByScoreArg.setMax(zScoreRangeArg.getMax());
        zRangeByScoreArg.setWithScores(withScores);
        zRangeByScoreArg.setLimit(limit);
        zRangeByScoreArg.setOffset(offset);
        zRangeByScoreArg.setCount(count);
        return zRangeByScoreArg;
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        Validates.validateArraysSize(commandName, arrays, 4, null);
    }

    @Override
    protected Resp2 doCommand0(ZRangeByScoreArg arg, ChannelHandlerContext ctx) {
        Client client = Attributes.getClient(ctx);
        Database database = client.getDatabase();
        ZSetObject zSetObject = Validates.validateType(database.get(arg.getKey()), ObjectType.ZSET);
        if (zSetObject == null) {
            return Arrays.EMPTY;
        }
        Collection<String> data = zSetObject.getOriginal().getDataByScoreRange(arg);
        if (data == null || data.isEmpty()) {
            return Arrays.EMPTY;
        }
        return Arrays.createByStringCollection(data);
    }
}
