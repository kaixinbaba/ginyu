import cmd.RedisCommands;
import common.Consoles;
import event.Events;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author: junjiexun
 * @date: 2020/10/14 6:06 下午
 * @description:
 */
public class JUnitDemo {

    @Test
    public void test() throws IOException {
        File f = new File("banner.txt");
        String s = FileUtils.readFileToString(f, StandardCharsets.UTF_8);
        Consoles.magenta(s);
        Consoles.trace("dfdf3\nadk");
        Consoles.debug("dkfjdkj\nadk");
        Consoles.info("hello\nworld");
        Consoles.warn("df\nworld");
        Consoles.error("102973lskdajf\nworld");
        Consoles.blue("dkfjakjdf");
        Consoles.gray("akdsjflkajd");
        Consoles.lightGray("akdsjflkajd");
        Consoles.white("dkfj");
        Consoles.magenta("dajkfjkdkfj");
    }

    @Test
    public void test1() throws InterruptedException {
//        System.out.println(RedisCommands.COMMAND_MAP);
        Events.post("abc");
        System.out.println("hello");
    }

    @Test
    public void test2() {
        ConcurrentSkipListSet<A> set = new ConcurrentSkipListSet<>();
        set.add(new A("adkfj"));
        set.add(new A("123"));
        set.add(new A("addj"));
        set.add(new A("adfkaj"));
        set.add(new A("fdkfj123"));
        set.add(new A("dsfk34"));
        System.out.println(set);
    }

    @Data
    @AllArgsConstructor
    private static class A implements Comparable<A> {
        private String name;

        @Override
        public int compareTo(A o) {
            return this.name.compareTo(o.name);
        }
    }
}
