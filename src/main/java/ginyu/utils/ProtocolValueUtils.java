package ginyu.utils;

import ginyu.protocol.Arrays;
import ginyu.protocol.BulkStrings;
import ginyu.protocol.Resp2;

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

    public static Long getLongFromBulkStringsInArrays(Arrays arrays, int index) {
        return Long.parseLong(getFromBulkStringsInArrays(arrays, index));
    }

    public static Double getDoubleFromBulkStringsInArrays(Arrays arrays, int index) {
        String content = getFromBulkStringsInArrays(arrays, index);
        if (content.equalsIgnoreCase("+inf")
                || content.equalsIgnoreCase("inf")) {
            return Double.POSITIVE_INFINITY;
        } else if (content.equalsIgnoreCase("-inf")) {
            return Double.NEGATIVE_INFINITY;
        } else {
            return Double.valueOf(content);
        }
    }
}
