package ginyu.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author: junjiexun
 * @date: 2020/10/11 9:48 下午
 * @description:
 */
@SuppressWarnings("all")
public class SimpleStringsTest {

    @Test
    public void convertTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
        byteBuf.writeBytes("OK\r\n".getBytes());
        Method convert = SimpleStrings.class.getDeclaredMethod("convert", ByteBuf.class);
        System.out.println(convert.invoke(null, byteBuf));
    }

}
