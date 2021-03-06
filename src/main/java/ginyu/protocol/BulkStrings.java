package ginyu.protocol;

import ginyu.common.Constants;
import ginyu.utils.ProtocolUtils;
import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.ToString;

import static ginyu.common.Constants.BULK_STRINGS_FLAG;
import static ginyu.common.Constants.SKIP;

/**
 * @author: junjiexun
 * @date: 2020/10/11 8:55 下午
 * @description:
 */
@Data
@ToString(callSuper = true)
@SuppressWarnings("all")
public class BulkStrings extends Resp2<BulkString> {

    public static final BulkStrings NULL = new BulkStrings(new BulkString(-1, null));

    public BulkStrings() {
        this(null);
    }

    public BulkStrings(BulkString bulkString) {
        if (bulkString != null) {
            this.setData(bulkString);
        }
        this.setFlag(BULK_STRINGS_FLAG);
    }

    public static BulkStrings create(Object content) {
        if (content == null) {
            return NULL;
        }
        return create(content.toString());
    }

    public static BulkStrings create(String content) {
        if (content == null) {
            return NULL;
        }
        return new BulkStrings(new BulkString(content.length(), content));
    }

    public static BulkStrings convert(ByteBuf byteBuf) {
        Integer length = ProtocolUtils.readInt(byteBuf);
        BulkStrings bulkStrings = new BulkStrings();
        String content;
        if (length < 0) {
            // eg: $-1\r\n
            content = null;
        } else if (length == 0) {
            // eg: $0\r\n\r\n
            content = "";
            byteBuf.skipBytes(SKIP);
        } else {
            // eg: $5\r\nginyu\r\n
            byte[] bb = new byte[length];
            byteBuf.readBytes(bb);
            content = new String(bb);
            byteBuf.skipBytes(SKIP);
        }
        bulkStrings.setData(new BulkString(length, content));
        return bulkStrings;
    }

    public static Resp2 defaultValue() {
        return BulkStrings.NULL;
    }

    @Override
    public void writeByteBuf(ByteBuf byteBuf) {
        byteBuf.writeBytes(this.getFlag().getBytes());
        BulkString bulkString = this.getData();
        byteBuf.writeBytes(String.valueOf(bulkString.getLength()).getBytes());
        byteBuf.writeBytes(Constants.SPLIT_BYTE);
        if (bulkString.getLength() == 0) {
            byteBuf.writeBytes(Constants.SPLIT_BYTE);
        } else if (bulkString.getLength() > 0 && bulkString.getContent() != null) {
            byteBuf.writeBytes(bulkString.getContent().getBytes());
            byteBuf.writeBytes(Constants.SPLIT_BYTE);
        }
    }
}