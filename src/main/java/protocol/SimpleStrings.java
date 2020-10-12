package protocol;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.ToString;
import utils.ProtocolUtils;

import static common.Constants.SIMPLE_STRINGS_FLAG;

/**
 * @author: junjiexun
 * @date: 2020/10/11 8:55 下午
 * @description:
 */
@Data
@ToString(callSuper = true)
public class SimpleStrings extends Resp2<String> {

    public SimpleStrings() {
        this.setFlag(SIMPLE_STRINGS_FLAG);
    }

    public static SimpleStrings convert(ByteBuf byteBuf) {
        SimpleStrings simpleStrings = new SimpleStrings();
        simpleStrings.setData(ProtocolUtils.readString(byteBuf));
        return simpleStrings;
    }
}
