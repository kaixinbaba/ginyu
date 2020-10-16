package protocol;

import common.Constants;
import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.ToString;
import utils.ProtocolUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static common.Constants.ARRAYS_FLAG;
import static common.Constants.STR_EMPTY_ARRAY;

/**
 * @author: junjiexun
 * @date: 2020/10/11 8:55 下午
 * @description:
 */
@Data
@ToString(callSuper = true)
@SuppressWarnings("all")
public class Arrays extends Resp2<List<Resp2>> {

    public static final Arrays EMPTY = Arrays.create(null);

    public static Arrays createSpecifiedSizeWithNull(int size) {
        List<Resp2> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            result.add(BulkStrings.NULL);
        }
        return create(result);
    }

    public static Arrays createByStringCollection(Collection<String> data) {
        return createByStringArray(data.toArray(STR_EMPTY_ARRAY));
    }

    public static Arrays createByStringArray(String... data) {
        return create(java.util.Arrays
                .stream(data)
                .map(s -> BulkStrings.create(s))
                .collect(Collectors.toList()));
    }

    public static Arrays create(List<Resp2> data) {
        Arrays arrays = new Arrays();
        arrays.setData(data);
        return arrays;
    }

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
        return map2String(false);
    }

    public List<String> map2String(boolean excludeCmd) {
        assert this.getData() != null;
        List<String> list = this.getData().stream().map(r -> {
            return ((BulkString) r.getData()).getContent();
        }).collect(Collectors.toList());
        if (excludeCmd) {
            list.remove(0);
        }
        return list;
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
