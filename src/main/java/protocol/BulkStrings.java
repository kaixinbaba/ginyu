package protocol;

import common.Constants;
import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.ToString;
import utils.ProtocolUtils;

import static common.Constants.BULK_STRINGS_FLAG;
import static common.Constants.SKIP;

/**
 * @author: junjiexun
 * @date: 2020/10/11 8:55 下午
 * @description:
 */
@Data
@ToString(callSuper = true)
public class BulkStrings extends Resp2<BulkString> {

    public BulkStrings() {
        this.setFlag(BULK_STRINGS_FLAG);
    }

    public static BulkStrings convert(ByteBuf byteBuf) {
        Integer length = ProtocolUtils.readInt(byteBuf);
        BulkStrings bulkStrings = new BulkStrings();
        String content;
        if (length < 0) {
            content = null;
        } else if (length == 0) {
            content = "";
            byteBuf.skipBytes(SKIP);
        } else {
            byte[] bb = new byte[length];
            byteBuf.readBytes(bb);
            content = new String(bb);
            byteBuf.skipBytes(SKIP);
        }
        bulkStrings.setData(new BulkString(length, content));
        return bulkStrings;
    }

    @Override
    public void writeByteBuf(ByteBuf byteBuf) {
        byteBuf.writeBytes(this.getFlag().getBytes());
        byteBuf.writeBytes(String.valueOf(this.getData().getLength()).getBytes());
        byteBuf.writeBytes(Constants.SPLIT_BYTE);
        byteBuf.writeBytes(this.getData().getContent().getBytes());
        byteBuf.writeBytes(Constants.SPLIT_BYTE);
    }
}