import common.Consoles;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author: junjiexun
 * @date: 2020/10/14 6:06 下午
 * @description:
 */
public class JUnitDemo {

    @Test
    public void test() throws IOException {
        File f = new File("banner.txt");
        String s = FileUtils.readFileToString(f, Charset.forName("UTF-8"));
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
}
