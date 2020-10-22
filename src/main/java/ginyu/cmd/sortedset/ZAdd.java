package ginyu.cmd.sortedset;

import ginyu.cmd.AbstractRedisCommand;
import ginyu.cmd.Command;
import ginyu.common.Attributes;
import ginyu.core.Client;
import ginyu.db.Database;
import ginyu.exception.CommandValidateException;
import ginyu.object.ObjectType;
import ginyu.object.StringObject;
import ginyu.object.ZSetNode;
import ginyu.object.ZSetObject;
import ginyu.protocol.*;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: junjiexun
 * @date: 2020/10/16 2:54 下午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "zadd")
public class ZAdd extends AbstractRedisCommand<ZAddArg, Integers> {

    @Override
    protected ZAddArg createArg(Arrays arrays) {
        List<String> args = arrays.map2String(true);
        String key = args.remove(0);
        Boolean xx = false;
        Boolean nx = false;
        Boolean ch = false;
        Boolean incr = false;
        List<String> nodePairs = new ArrayList<>();
        for (String arg : args) {
            if (arg.equalsIgnoreCase("XX")) {
                xx = true;
            } else if (arg.equalsIgnoreCase("NX")) {
                nx = true;
            } else if (arg.equalsIgnoreCase("CH")) {
                ch = true;
            } else if (arg.equalsIgnoreCase("INCR")) {
                incr = true;
            } else {
                nodePairs.add(arg);
            }
        }
        // validate
        if (xx && nx) {
            throw new CommandValidateException("'XX', 'NX' can not both exists");
        }
        if (incr && nodePairs.size() != 2) {
            throw new CommandValidateException("INCR option supports a single increment-element pair");
        }
        if (nodePairs.size() % 2 != 0) {
            throw new CommandValidateException("'score' and 'member' must appear in pairs");
        }
        ZSetNode[] nodes = new ZSetNode[nodePairs.size() / 2];
        for (int i = 0; i < nodePairs.size(); i += 2) {
            nodes[i / 2] = new ZSetNode(
                    nodePairs.get(i + 1),
                    Double.valueOf(nodePairs.get(i))
            );
        }
        ZAddArg zAddArg = new ZAddArg();
        zAddArg.setKey(key);
        zAddArg.setNx(nx);
        zAddArg.setXx(xx);
        zAddArg.setCh(ch);
        zAddArg.setIncr(incr);
        zAddArg.setNodes(nodes);
        return zAddArg;
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        Validates.validateArraysSize(commandName, arrays, 4, null);
    }

    @Override
    protected Resp2 doCommand0(ZAddArg arg, ChannelHandlerContext ctx) {
        Client client = Attributes.getClient(ctx);
        Database database = client.getDatabase();
        ZSetObject zSetObject = Validates.validateType(database.get(arg.getKey()), ObjectType.ZSET);
        if (zSetObject == null) {
            if (arg.getXx()) {
                return Integers.ZERO;
            }
            zSetObject = new ZSetObject();
            database.set(arg.getKey(), zSetObject);
            return add(zSetObject, arg);
        } else {
            if (arg.getNx()) {
                return Integers.ZERO;
            }
            return add(zSetObject, arg);
        }
    }

    private Resp2 add(ZSetObject zSetObject, ZAddArg arg) {
        return zSetObject.getOriginal().add(arg.getCh(), arg.getIncr(), arg.getNodes());
    }
}
