package protocol;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.ToString;
import utils.ProtocolUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static common.Constants.ARRAYS_FLAG;

/**
 * @author: junjiexun
 * @date: 2020/10/11 8:55 下午
 * @description:
 */
@Data
@ToString(callSuper = true)
@SuppressWarnings("all")
public class Arrays extends Resp2<List> {

    public Arrays() {
        this.setFlag(ARRAYS_FLAG);
    }

    public static Arrays convert(ByteBuf byteBuf) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Integer listSize = ProtocolUtils.readInt(byteBuf);
        Arrays arrays = new Arrays();
        List data = new ArrayList(listSize);
        for (int i = 0; i < listSize; i++) {
            data.add(Serializers.decode(byteBuf));
        }
        arrays.setData(data);
        return arrays;
    }
}
