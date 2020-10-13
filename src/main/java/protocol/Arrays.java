package protocol;

import common.Constants;
import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.ToString;
import utils.ProtocolUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static common.Constants.ARRAYS_FLAG;

/**
 * @author: junjiexun
 * @date: 2020/10/11 8:55 下午
 * @description:
 */
@Data
@ToString(callSuper = true)
@SuppressWarnings("all")
public class Arrays extends Resp2<List<Resp2>> {

    public Arrays() {
        this.setFlag(ARRAYS_FLAG);
    }

    public static Arrays convert(ByteBuf byteBuf) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Integer listSize = ProtocolUtils.readInt(byteBuf);
        Arrays arrays = new Arrays();
        List<Resp2> data = new ArrayList<>(listSize);
        for (int i = 0; i < listSize; i++) {
            data.add(Serializers.decode(byteBuf));
        }
        arrays.setData(data);
        return arrays;
    }

    public List<String> map2String() {
        assert this.getData() != null;
        return this.getData().stream().map(r -> {
            return ((BulkString)r.getData()).getContent();
        }).collect(Collectors.toList());
    }

    @Override
    public void writeByteBuf(ByteBuf byteBuf) {
        byteBuf.writeBytes(this.getFlag().getBytes());
        List<Resp2> data = this.getData();
        byteBuf.writeBytes(String.valueOf(data.size()).getBytes());
        byteBuf.writeBytes(Constants.SPLIT_BYTE);
        for (Resp2 resp2 : data) {
            resp2.writeByteBuf(byteBuf);
        }
    }
}
