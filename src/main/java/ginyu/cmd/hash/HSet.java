package ginyu.cmd.hash;

import ginyu.cmd.AbstractRedisCommand;
import ginyu.cmd.Command;
import ginyu.common.Attributes;
import ginyu.core.Client;
import ginyu.db.Database;
import ginyu.exception.CommandValidateException;
import ginyu.object.Dict;
import ginyu.object.HashObject;
import ginyu.object.ObjectType;
import ginyu.protocol.Arrays;
import ginyu.protocol.Integers;
import ginyu.protocol.Resp2;
import ginyu.protocol.Validates;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

/**
 * @author: junjiexun
 * @date: 2020/10/13 1:56 下午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "hset")
public class HSet extends AbstractRedisCommand<HSetArg, Integers> {
    @Override
    public HSetArg createArg(Arrays arrays) {
        List<String> argList = arrays.map2String(true);
        return new HSetArg(
                argList.remove(0),
                getHashEntry(argList));
    }

    private HashEntry[] getHashEntry(List<String> argList) {
        final int argSize = argList.size();
        HashEntry[] hashEntries = new HashEntry[argSize / 2];
        for (int i = 0; i < argSize; i += 2) {
            hashEntries[i / 2] = new HashEntry(argList.get(i), argList.get(i + 1));
        }
        return hashEntries;
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        Validates.validateArraysSize(commandName, arrays, 4, null);
        if ((arrays.getData().size() - 2) % 2 != 0) {
            throw new CommandValidateException("field and value must appear in pairs");
        }
    }

    @Override
    protected Resp2 doCommand0(HSetArg arg, ChannelHandlerContext ctx) {
        Client client = Attributes.getClient(ctx);
        Database database = client.getDatabase();
        HashObject hashObject = Validates.validateType(database.get(arg.getKey()), ObjectType.HASH);
        if (hashObject == null) {
            hashObject = new HashObject();
            database.set(arg.getKey(), hashObject);
        }
        int newFieldCount = 0;
        for (HashEntry entry : arg.getEntries()) {
            Dict<String, String> original = hashObject.getOriginal();
            if (!original.containsKey(entry.getField())) {
                newFieldCount++;
            }
            original.put(entry.getField(), entry.getValue());
        }
        return Integers.create(newFieldCount);
    }
}
