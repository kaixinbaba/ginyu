package protocol;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.ToString;
import utils.ProtocolUtils;

import static common.Constants.INTEGERS_FLAG;

/**
 * @author: junjiexun
 * @date: 2020/10/11 8:55 下午
 * @description:
 */
@Data
@ToString(callSuper = true)
public class Integers extends Resp2<Integer> {

    public Integers() {
        this.setFlag(INTEGERS_FLAG);
    }

    public static Integers convert(ByteBuf byteBuf) {
        Integers integers = new Integers();
        integers.setData(ProtocolUtils.readInt(byteBuf));
        return integers;
    }
}
