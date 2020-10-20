package ginyu.cmd.list;

import com.google.common.eventbus.Subscribe;
import ginyu.core.Client;
import ginyu.db.Database;
import ginyu.event.BlockEvent;
import ginyu.event.list.BlockByBPopEvent;
import ginyu.event.list.PushEvent;
import ginyu.object.ListObject;
import ginyu.object.ObjectType;
import ginyu.protocol.Arrays;
import ginyu.protocol.BulkStrings;
import ginyu.protocol.Resp2;
import ginyu.protocol.Validates;
import ginyu.utils.BlockUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author: junjiexun
 * @date: 2020/10/20 1:41 下午
 * @description:
 */
@SuppressWarnings("all")
public class ListListener {

    @Subscribe
    public void blockByBPopEvent(BlockByBPopEvent event) {
        Client client = event.getClient();
        Set<String> keys = new HashSet<>(event.getArg().getKeys().length);
        Collections.addAll(keys, event.getArg().getKeys());
        client.addBlockKeys(keys);
        for (String key : keys) {
            client.getDatabase().addToBlockingDict(key, event);
        }
    }

    @Subscribe
    public void pushEvent(PushEvent event) {
        Database database = event.getClient().getDatabase();
        Set<BlockEvent> blockEventSet = database.getBlockedEvents(event.getKey());
        if (blockEventSet == null || blockEventSet.isEmpty()) {
            return;
        }
        int i = 0;
        for (BlockEvent blockEvent : blockEventSet) {
            if (!(blockEvent instanceof BlockByBPopEvent)) {
                continue;
            }
            if (i >= event.getListSize()) {
                return;
            }
            BlockByBPopEvent blockByBPopEvent = (BlockByBPopEvent) blockEvent;
            ListObject listObject = Validates.validateType(blockByBPopEvent.getClient().getDatabase().get(event.getKey()),
                    ObjectType.LIST);
            if (listObject == null) {
                return;
            }
            String pop = listObject.getOriginal().pop(blockByBPopEvent.getIsLeft());
            if (pop == null) {
                return;
            }
            // unblock一定要成功的pop出元素才能使客户端解除阻塞
            Resp2 resp2 = Arrays.createByStringArray(event.getKey(), pop);
            blockByBPopEvent.getClient().getCtx().writeAndFlush(resp2);
            BlockUtils.clearBlockClient(blockByBPopEvent);
            if (listObject.getOriginal().isEmpty()) {
                database.delete(event.getKey());
            }
            i++;
        }
    }
}
