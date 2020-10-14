package utils;

import protocol.Arrays;
import protocol.BulkStrings;
import protocol.Resp2;

/**
 * @author: junjiexun
 * @date: 2020/10/14 5:32 下午
 * @description:
 */
@SuppressWarnings("all")
public abstract class ProtocolValueUtils {

    public static <T extends Resp2> T getFromArrays(Arrays arrays, int index) {
        return (T) arrays.getData().get(index);
    }

    public static String getFromBulkStringsInArrays(Arrays arrays, int index) {
        return ProtocolValueUtils.<BulkStrings>getFromArrays(arrays, index).getData().getContent();
    }

    public static Integer getIntFromBulkStringsInArrays(Arrays arrays, int index) {
        return Integer.parseInt(getFromBulkStringsInArrays(arrays, index));
    }
}