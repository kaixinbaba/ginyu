package ginyu.protocol;

import ginyu.common.Constants;
import ginyu.utils.ProtocolUtils;
import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.ToString;

import static ginyu.common.Constants.SIMPLE_STRINGS_FLAG;

/**
 * @author: junjiexun
 * @date: 2020/10/11 8:55 下午
 * @description:
 */
@Data
@ToString(callSuper = true)
@SuppressWarnings("all")
public class SimpleStrings extends Resp2<String> {

    public static final SimpleStrings OK = SimpleStrings.create("OK");
    public static final SimpleStrings NONE = SimpleStrings.create("none");

    public SimpleStrings() {
        this(null);
    }

    public SimpleStrings(String data) {
        if (data != null) {
            this.setData(data);
        }
        this.setFlag(SIMPLE_STRINGS_FLAG);
    }

    public static SimpleStrings create(String content) {
        SimpleStrings simpleStrings = new SimpleStrings();
        simpleStrings.setData(content);
        return simpleStrings;
    }

    public static SimpleStrings convert(ByteBuf byteBuf) {
        SimpleStrings simpleStrings = new SimpleStrings();
        simpleStrings.setData(ProtocolUtils.readString(byteBuf));
        return simpleStrings;
    }

    public static Resp2 defaultValue() {
        return SimpleStrings.OK;
    }

    @Override
    public void writeByteBuf(ByteBuf byteBuf) {
        byteBuf.writeBytes(this.getFlag().getBytes());
        byteBuf.writeBytes(String.valueOf(this.getData()).getBytes());
        byteBuf.writeBytes(Constants.SPLIT_BYTE);
    }
}
