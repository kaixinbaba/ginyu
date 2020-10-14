import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: junjiexun
 * @date: 2020/10/14 6:06 下午
 * @description:
 */
public class JUnitDemo {

    @Test
    public void test() {
        Map<String, String> map = new HashMap<>();
        String remove = map.remove("1");
        System.out.println(remove);
    }
}
