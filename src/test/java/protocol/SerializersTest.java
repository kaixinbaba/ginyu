package protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.Assert;
import org.junit.Test;
import utils.ProtocolUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * @author: junjiexun
 * @date: 2020/10/11 9:48 下午
 * @description:
 */
@SuppressWarnings("all")
public class SerializersTest {

    @Test
    public void test() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
        byteBuf.writeBytes("*3\r\n$3\r\nxun\r\n$6\r\nlaoxun\r\n:3\r\n".getBytes());
        System.out.println(ProtocolUtils.byte2ascii(byteBuf.getByte(4)));
        System.out.println(byteBuf.readableBytes());
        byteBuf.skipBytes(10);
        System.out.println(ProtocolUtils.byte2ascii(byteBuf.getByte(4)));
        System.out.println(byteBuf.readableBytes());
        byteBuf.release();
    }

    @Test
    public void convertSimpleStringTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
        byteBuf.writeBytes("+OK\r\n".getBytes());
        Resp2 decode = Serializers.decode(byteBuf);
        Assert.assertEquals("OK", decode.getData());
        Assert.assertTrue(decode instanceof SimpleStrings);
    }

    @Test
    public void convertErrorsTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
        byteBuf.writeBytes("-ERR value is not an integer or out of range\r\n".getBytes());
        Resp2 decode = Serializers.decode(byteBuf);
        Assert.assertEquals("ERR value is not an integer or out of range", decode.getData());
        Assert.assertTrue(decode instanceof Errors);
    }

    @Test
    public void convertErrors2Test() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
        byteBuf.writeBytes("-error\r\n".getBytes());
        Resp2 decode = Serializers.decode(byteBuf);
        Assert.assertEquals("error", decode.getData());
        Assert.assertTrue(decode instanceof Errors);
    }

    @Test
    public void convertIntegersTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
        byteBuf.writeBytes(":789\r\n".getBytes());
        Resp2 decode = Serializers.decode(byteBuf);
        Assert.assertEquals(789, decode.getData());
        Assert.assertTrue(decode instanceof Integers);
    }

    @Test
    public void convertBulkStringsTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
        byteBuf.writeBytes("$6\r\nlaoxun\r\n".getBytes());
        Resp2 decode = Serializers.decode(byteBuf);
        Assert.assertTrue(decode instanceof BulkStrings);

        Assert.assertEquals(6, (int) ((BulkStrings) decode).getData().getLength());
        Assert.assertEquals("laoxun", ((BulkStrings) decode).getData().getContent());
    }

    @Test
    public void convertArraysTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
        byteBuf.writeBytes("*3\r\n$3\r\nxun\r\n$6\r\nlaoxun\r\n:3\r\n".getBytes());
        Resp2 decode = Serializers.decode(byteBuf);
        Assert.assertTrue(decode instanceof Arrays);
        Arrays arrays = (Arrays) decode;
        Assert.assertEquals(3, arrays.getData().size());
    }

    @Test
    public void convertArrays2Test() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
        byteBuf.writeBytes("*3\r\n*2\r\n$3\r\nxun\r\n*2\r\n+OK\r\n-ErrorKexuejia\r\n$13\r\ndaimakexuejia\r\n$6\r\nlaoxun\r\n:339471239\r\n".getBytes());
        Resp2 decode = Serializers.decode(byteBuf);
        System.out.println(decode);
        Assert.assertTrue(decode instanceof Arrays);
        Arrays arrays = (Arrays) decode;
        Assert.assertEquals(3, arrays.getData().size());
    }

}
