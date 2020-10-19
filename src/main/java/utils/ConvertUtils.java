package utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author: junjiexun
 * @date: 2020/10/18 8:54 下午
 * @description:
 */
public abstract class ConvertUtils {

    public static <K, V> Map<K, V> set2map(Set<K> source, V v) {
        Map<K, V> result = new HashMap<>(source.size());
        for (K k : source) {
            result.put(k, v);
        }
        return result;
    }

    public static int getPositiveIndex(int index, int total) {
        if (index >= 0) {
            return index;
        }
        return total + index;
    }

    public static int getCorrectIndex(int index, int total) {
        int positiveIndex = getPositiveIndex(index, total);
        if (positiveIndex < 0) {
            positiveIndex = 0;
        }
        if (positiveIndex >= total) {
            positiveIndex = total - 1;
        }
        return positiveIndex;
    }
}
