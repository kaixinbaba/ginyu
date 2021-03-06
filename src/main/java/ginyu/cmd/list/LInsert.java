package ginyu.cmd.list;

import ginyu.cmd.AbstractRedisCommand;
import ginyu.cmd.Command;
import ginyu.common.Attributes;
import ginyu.core.Client;
import ginyu.db.Database;
import ginyu.exception.CommandValidateException;
import ginyu.object.List;
import ginyu.object.ListObject;
import ginyu.object.ObjectType;
import ginyu.protocol.Arrays;
import ginyu.protocol.Integers;
import ginyu.protocol.Resp2;
import ginyu.protocol.Validates;
import ginyu.utils.ProtocolValueUtils;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingDeque;

import static ginyu.common.Constants.STR_EMPTY_ARRAY;

/**
 * @author: junjiexun
 * @date: 2020/10/19 2:17 下午
 * @description: FIXME 这个命令有非常严重的性能问题
 * 如果不替换底层的数据结构自己实现的话，java自带的LinkedBlockingDeque是没有索引的功能的，
 * 并且只能在头尾节点入队或出队，无法在指定位置进行插入，只能将队列转成数组再转成ArrayList再插入，最后再转换回双端队列
 */
@SuppressWarnings("all")
@Command(name = "linsert")
public class LInsert extends AbstractRedisCommand<LInsertArg, Integers> {

    private static final String BEFORE = "before";
    private static final String AFTER = "after";

    @Override
    protected LInsertArg createArg(Arrays arrays) {
        return new LInsertArg(ProtocolValueUtils.getFromBulkStringsInArrays(arrays, 1),
                ProtocolValueUtils.getFromBulkStringsInArrays(arrays, 2).toLowerCase().equals(BEFORE),
                ProtocolValueUtils.getFromBulkStringsInArrays(arrays, 3),
                ProtocolValueUtils.getFromBulkStringsInArrays(arrays, 4));
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        Validates.validateArraysSize(commandName, arrays, 5);
        String position = ProtocolValueUtils.getFromBulkStringsInArrays(arrays, 2).toLowerCase();
        if (!BEFORE.equals(position) && !AFTER.equals(position)) {
            throw new CommandValidateException("POSITION enums must equal 'BEFORE' or 'AFTER'");
        }
    }

    @Override
    protected Resp2 doCommand0(LInsertArg arg, ChannelHandlerContext ctx) {
        Client client = Attributes.getClient(ctx);
        Database database = client.getDatabase();
        ListObject listObject = Validates.validateType(database.get(arg.getKey()), ObjectType.LIST);
        if (listObject == null) {
            return Integers.ZERO;
        }
        List list = listObject.getOriginal();
        int pivotIndex = list.indexOf(arg.getPivot());
        if (pivotIndex < 0) {
            // 没找到
            return Integers.N_ONE;
        }
        ArrayList<String> newList = new ArrayList<String>(java.util.Arrays.asList(list.getList().toArray(STR_EMPTY_ARRAY)));
        newList.add(arg.getIsBefore() ? pivotIndex : pivotIndex + 1, arg.getValue());
        list.setList(new LinkedBlockingDeque<>(newList));
        return Integers.create(list.size());
    }
}
