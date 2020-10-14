package utils;

import common.Constants;
import io.netty.buffer.ByteBuf;

import javax.sound.midi.Soundbank;

import static common.Constants.*;

/**
 * @author: junjiexun
 * @date: 2020/10/12 5:29 下午
 * @description:
 */
@SuppressWarnings("all")
public abstract class ProtocolUtils {

    public static boolean isProtocolBegin(String content) {
        return content.startsWith(SIMPLE_STRINGS_FLAG)
                || content.startsWith(ERRORS_FLAG)
                || content.startsWith(BULK_STRINGS_FLAG)
                || content.startsWith(INTEGERS_FLAG)
                || content.startsWith(ARRAYS_FLAG);
    }

    public static String readFirstString(ByteBuf byteBuf) {
        return byte2ascii(byteBuf.readByte());
    }

    public static String byte2ascii(byte b) {
        return String.valueOf((char) b);
    }

    public static String readString(ByteBuf byteBuf) {
        int start = byteBuf.readerIndex();
        int end = byteBuf.readableBytes();
        for (int i = start; i < start + end; i++) {
            String s = ProtocolUtils.byte2ascii(byteBuf.getByte(i));
            if (s.equals(CR)) {
                byte[] bb = new byte[i - start];
                byteBuf.readBytes(bb);
                byteBuf.skipBytes(Constants.SKIP);
                return new String(bb);
            }
        }
        return null;
    }

    public static Integer readInt(ByteBuf byteBuf) {
        try {
            return Integer.parseInt(readString(byteBuf));
        } catch (Exception e) {
            throw e;
        }
    }


}
