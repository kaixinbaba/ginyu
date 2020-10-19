package ginyu.protocol;

import ginyu.utils.ProtocolUtils;
import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.ToString;

import static ginyu.common.Constants.ERRORS_FLAG;

/**
 * @author: junjiexun
 * @date: 2020/10/11 8:55 下午
 * @description:
 */
@SuppressWarnings("all")
@Data
@ToString(callSuper = true)
public class Errors extends SimpleStrings {

    public static final Errors ERROR = Errors.create("Error");

    public Errors() {
        this.setFlag(ERRORS_FLAG);
    }

    public static Errors convert(ByteBuf byteBuf) {
        Errors errors = new Errors();
        errors.setData(ProtocolUtils.readString(byteBuf));
        return errors;
    }

    public static Errors create(String errorMessage) {
        Errors errors = new Errors();
        errors.setData(errorMessage);
        return errors;
    }

    public static Resp2 defaultValue() {
        return Errors.ERROR;
    }
}
