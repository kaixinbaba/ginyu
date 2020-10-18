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
}
