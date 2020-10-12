package protocol;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.ToString;
import utils.ProtocolUtils;

import static common.Constants.ERRORS_FLAG;

/**
 * @author: junjiexun
 * @date: 2020/10/11 8:55 下午
 * @description:
 */
@Data
@ToString(callSuper = true)
public class Errors extends Resp2<String> {

    public Errors() {
        this.setFlag(ERRORS_FLAG);
    }

    public static Errors convert(ByteBuf byteBuf) {
        Errors errors = new Errors();
        errors.setData(ProtocolUtils.readString(byteBuf));
        return errors;
    }
}
