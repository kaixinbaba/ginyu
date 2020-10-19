package ginyu.protocol;

import io.netty.buffer.ByteBuf;
import lombok.Data;

/**
 * @author: junjiexun
 * @date: 2020/10/11 8:44 下午
 * @description:
 */
@Data
public abstract class Resp2<T> {

    private String flag;

    private T data;

    public abstract void writeByteBuf(ByteBuf byteBuf);
}
