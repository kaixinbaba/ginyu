package protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import utils.ProtocolUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static common.Constants.*;

/**
 * @author: junjiexun
 * @date: 2020/10/11 9:48 下午
 * @description:
 */
@SuppressWarnings("all")
public abstract class Serializers {

    private final static Map<String, Class> RESP_FACTORY = new HashMap<>();

    static {
        RESP_FACTORY.put(SIMPLE_STRINGS_FLAG, SimpleStrings.class);
        RESP_FACTORY.put(ERRORS_FLAG, Errors.class);
        RESP_FACTORY.put(INTEGERS_FLAG, Integers.class);
        RESP_FACTORY.put(BULK_STRINGS_FLAG, BulkStrings.class);
        RESP_FACTORY.put(ARRAYS_FLAG, Arrays.class);
    }

    public static ByteBuf encode(Resp2 resp) {
        // 1. 创建 ByteBuf 对象
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
        // 2. 序列化 Java 对象
//        byte[] bytes = serializer.serialize(msg);
//
//        // 3. 实际编码过程
//        byteBuf.writeInt(MAGIC_NUMBER);
//        byteBuf.writeByte(packet.getVersion());
//        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
//        byteBuf.writeByte(packet.getCommand());
//        byteBuf.writeInt(bytes.length);
//        byteBuf.writeBytes(bytes);
        byteBuf.writeBytes("+OK\r\n".getBytes());
        return byteBuf;
    }

    public static Resp2 decode(ByteBuf byteBuf) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String flag = ProtocolUtils.readFirstString(byteBuf);
        Class clazz = RESP_FACTORY.get(flag);
        Method convert = clazz.getDeclaredMethod("convert", ByteBuf.class);
        return (Resp2) convert.invoke(null, byteBuf);
    }

}
