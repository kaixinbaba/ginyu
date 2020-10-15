package cmd.hash;

import cmd.AbstractRedisCommand;
import cmd.Command;
import common.Attributes;
import core.Client;
import core.Server;
import db.Database;
import exception.CommandValidateException;
import io.netty.channel.ChannelHandlerContext;
import object.Dict;
import object.HashObject;
import object.ObjectType;
import protocol.Arrays;
import protocol.Integers;
import protocol.Resp2;
import protocol.Validates;

import java.util.List;

/**
 * @author: junjiexun
 * @date: 2020/10/13 1:56 下午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "hset")
public class HSet extends AbstractRedisCommand<HSetArg> {
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
        Database database = Server.INSTANCE.getDb().getDatabase(client.getDb());
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
