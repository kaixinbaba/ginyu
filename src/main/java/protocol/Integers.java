package protocol;

import common.Constants;
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
@SuppressWarnings("all")
public class Integers extends Resp2<Long> {

    public static final Integers N_TWO = Integers.create(-2L);
    public static final Integers N_ONE = Integers.create(-1L);
    public static final Integers ZERO = Integers.create(0L);
    public static final Integers ONE = Integers.create(1L);

    public Integers() {
        this.setFlag(INTEGERS_FLAG);
    }

    public static Integers convert(ByteBuf byteBuf) {
        Integers integers = new Integers();
        integers.setData(ProtocolUtils.readLong(byteBuf));
        return integers;
    }

    public static Integers create(Integer number) {
        return create((long) number);
    }

    public static Integers create(Long number) {
        Integers integers = new Integers();
        integers.setData(number);
        return integers;
    }

    public static Resp2 defaultValue() {
        return Integers.ZERO;
    }

    @Override
    public void writeByteBuf(ByteBuf byteBuf) {
        byteBuf.writeBytes(this.getFlag().getBytes());
        byteBuf.writeBytes(String.valueOf(this.getData()).getBytes());
        byteBuf.writeBytes(Constants.SPLIT_BYTE);
    }
}
