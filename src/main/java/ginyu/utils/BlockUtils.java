package ginyu.utils;

import ginyu.event.BlockEvent;
import ginyu.event.list.BlockByBPopEvent;

import java.util.Set;

/**
 * @author: junjiexun
 * @date: 2020/10/20 2:53 下午
 * @description:
 */
public abstract class BlockUtils {

    public static void clearBlockClient(BlockByBPopEvent blockByBPopEvent) {
        Set<String> blockKeys = blockByBPopEvent.getClient().getBlockKeys();
        if (blockKeys == null || blockKeys.isEmpty()) {
            return;
        }
        for (String blockKey : blockKeys) {
            clearBlockClientInDatabase(blockByBPopEvent, blockKey);
        }
        blockByBPopEvent.getClient().clearBlock();
    }

    public static void clearBlockClientInDatabase(BlockByBPopEvent blockByBPopEvent, String key) {
        Set<BlockEvent> blockedEvents = blockByBPopEvent.getClient().getDatabase().getBlockedEvents(key);
        if (blockedEvents == null || blockedEvents.isEmpty()) {
            return;
        }
        blockedEvents.remove(blockByBPopEvent);
    }
}
