package protocol;

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
        byte[] bb = new byte[length];
        byteBuf.readBytes(bb);
        byteBuf.skipBytes(SKIP);
        BulkStrings bulkStrings = new BulkStrings();
        bulkStrings.setData(new BulkString(length, new String(bb)));
        return bulkStrings;
    }
}