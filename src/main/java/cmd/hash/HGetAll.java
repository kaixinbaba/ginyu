package cmd.hash;

import cmd.AbstractRedisCommand;
import cmd.Command;
import cmd.KeyArg;
import common.Attributes;
import core.Client;
import core.Server;
import db.Database;
import io.netty.channel.ChannelHandlerContext;
import object.Dict;
import object.HashObject;
import object.ObjectType;
import protocol.Arrays;
import protocol.Resp2;
import protocol.Validates;
import utils.ProtocolValueUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: junjiexun
 * @date: 2020/10/13 1:56 下午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "hgetall")
public class HGetAll extends AbstractRedisCommand<KeyArg, Arrays> {
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
        boolean expired = database.checkIfExpired(arg.getKey());
        if (expired) {
            database.delete(arg.getKey());
            return Arrays.EMPTY;
        }
        HashObject hashObject = Validates.validateType(database.get(arg.getKey()), ObjectType.HASH);
        if (hashObject == null) {
            return Arrays.EMPTY;
        }
        Dict<String, String> original = hashObject.getOriginal();
        List<String> result = new ArrayList<>(original.size() * 2);
        for (Map.Entry<String, String> entry : original.entrySet()) {
            result.add(entry.getKey());
            result.add(entry.getValue());
        }
        // FIXME redis是按照hset的顺序返回的，而我是乱序的
        return Arrays.createByStringCollection(result);
    }
}
