package ginyu.protocol;

import ginyu.utils.ProtocolUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

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
    public void convertBulkStrings2Test() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
        byteBuf.writeBytes("$0\r\n\r\n".getBytes());
        Resp2 decode = Serializers.decode(byteBuf);
        System.out.println(decode);
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
        byteBuf.writeBytes("*3\r\n*2\r\n$3\r\nxun\r\n*2\r\n$0\r\n\r\n$-1\r\n$13\r\ndaimakexuejia\r\n$6\r\nlaoxun\r\n:339471239\r\n".getBytes());
        Resp2 decode = Serializers.decode(byteBuf);
        System.out.println(decode);
        Assert.assertTrue(decode instanceof Arrays);
        Arrays arrays = (Arrays) decode;
        Assert.assertEquals(3, arrays.getData().size());
    }

    @Test
    public void decodeSimpleStringsTest() {
        SimpleStrings simpleStrings = new SimpleStrings();
        simpleStrings.setData("Bye Bye");
        ByteBuf encode = Serializers.encode(simpleStrings);
        byte[] bb = new byte[encode.readableBytes()];
        encode.readBytes(bb);
        Assert.assertEquals("+Bye Bye\r\n", new String(bb));
    }

    @Test
    public void decodeErrorsTest() {
        Errors errors = new Errors();
        errors.setData("Error Info exception");
        ByteBuf encode = Serializers.encode(errors);
        byte[] bb = new byte[encode.readableBytes()];
        encode.readBytes(bb);
        Assert.assertEquals("-Error Info exception\r\n", new String(bb));
    }

    @Test
    public void decodeIntegersTest() {
        Integers integers = new Integers();
        integers.setData(777L);
        ByteBuf encode = Serializers.encode(integers);
        byte[] bb = new byte[encode.readableBytes()];
        encode.readBytes(bb);
        Assert.assertEquals(":777\r\n", new String(bb));
    }

    @Test
    public void decodeBulkStringsTest() {
        BulkStrings bulkStrings = new BulkStrings();
        bulkStrings.setData(new BulkString(13, "daimakexuejia"));
        ByteBuf encode = Serializers.encode(bulkStrings);
        byte[] bb = new byte[encode.readableBytes()];
        encode.readBytes(bb);
        Assert.assertEquals("$13\r\ndaimakexuejia\r\n", new String(bb));
    }

    @Test
    public void decodeArraysTest() {
        Arrays arrays = new Arrays();
        List<Resp2> data = new ArrayList<>();
        Integers integers = new Integers();
        integers.setData(777L);
        data.add(integers);
        BulkStrings bulkStrings = new BulkStrings();
        bulkStrings.setData(new BulkString(13, "daimakexuejia"));
        data.add(bulkStrings);

        Arrays arrays2 = new Arrays();
        List<Resp2> data2 = new ArrayList<>();
        Integers integers2 = new Integers();
        integers2.setData(777L);
        data2.add(integers2);
        BulkStrings bulkStrings2 = new BulkStrings();
        bulkStrings2.setData(new BulkString(13, "daimakexuejia"));
        data2.add(bulkStrings2);
        arrays2.setData(data2);

        data.add(arrays2);
        arrays.setData(data);
        ByteBuf encode = Serializers.encode(arrays);
        byte[] bb = new byte[encode.readableBytes()];
        encode.readBytes(bb);
        Assert.assertEquals("*3\r\n:777\r\n$13\r\ndaimakexuejia\r\n*2\r\n:777\r\n$13\r\ndaimakexuejia\r\n", new String(bb));
    }

}
