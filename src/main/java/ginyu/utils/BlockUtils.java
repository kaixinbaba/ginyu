package ginyu.utils;

import ginyu.event.BlockEvent;

import java.util.Set;

/**
 * @author: junjiexun
 * @date: 2020/10/20 2:53 下午
 * @description:
 */
public abstract class BlockUtils {

    public static void clearBlockClient(BlockEvent blockEvent) {
        Set<String> blockKeys = blockEvent.getClient().getBlockKeys();
        if (blockKeys == null || blockKeys.isEmpty()) {
            return;
        }
        for (String blockKey : blockKeys) {
            clearBlockClientInDatabase(blockEvent, blockKey);
        }
        blockEvent.getClient().clearBlock();
    }

    public static void clearBlockClientInDatabase(BlockEvent blockEvent, String key) {
        Set<BlockEvent> blockedEvents = blockEvent.getClient().getDatabase().getBlockedEvents(key);
        if (blockedEvents == null || blockedEvents.isEmpty()) {
            return;
        }
        blockedEvents.remove(blockEvent);
        if (blockEvent.getClient().getDatabase().getBlockedEvents(key).isEmpty()) {
            blockEvent.getClient().getDatabase().removeBlockKey(key);
        }
    }
}
