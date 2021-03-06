import com.google.common.collect.MinMaxPriorityQueue;
import ginyu.common.Consoles;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

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
        Map<String, Map<String, List<String>>> map = new HashMap<>();
        Map<String, List<String>> mmap = new HashMap<>();
        List<String> ls = new ArrayList<>();
        ls.add("1");
        ls.add("2");
        ls.add("3");
        mmap.put("aa", ls);
        map.put("a", mmap);

        Map<String, Map<String, List<String>>> pam = new HashMap<>(map);

        Map<String, List<String>> a = pam.get("a");
        List<String> aa = a.get("aa");
        aa.add("4");


        System.out.println(map);
        System.out.println(pam);


    }

    @Test
    public void test2() {
        System.out.println(Double.valueOf("inf"));
    }

    @Data
    @AllArgsConstructor
    private static class A implements Comparable<A> {
        private String name;

        private Double score;

        @Override
        public int hashCode() {
            return this.name.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof A)) {
                return false;
            }
            return this.name.equals(((A) obj).name);
        }

        @Override
        public int compareTo(A o) {
            int scoreResult = this.score.compareTo(o.score);
            if (scoreResult == 0) {
                return this.name.compareTo(o.name);
            }
            return scoreResult;
        }
    }

    @Test
    public void writeByteTest() throws IOException {
        File f = new File("./tttt");
        if (!f.exists()) {
            f.createNewFile();
        }
        for (int i = 0; i < 10; i++) {
            FileUtils.writeByteArrayToFile(f, ("123 adfkj" + i).getBytes(), true);
        }
    }
}
