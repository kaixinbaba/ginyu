import org.junit.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author: junjiexun
 * @date: 2020/10/14 6:06 下午
 * @description:
 */
public class JUnitDemo {

    @Test
    public void test() {
        ConcurrentSkipListMap<Long, String> map = new ConcurrentSkipListMap<>();
        map.put(7L, "");
        map.put(11L, "");
        map.put(3L, "");
        map.put(4L, "");
        map.put(1L, "");
        map.put(9L, "");
        System.out.println(map);
        for (Map.Entry<Long, String> longStringEntry : map.entrySet()) {
            System.out.println(longStringEntry.getKey() + " == " + longStringEntry.getValue());
        }
    }
}
