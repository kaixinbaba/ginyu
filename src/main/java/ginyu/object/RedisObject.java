package ginyu.object;

import ginyu.core.Snapshot;
import lombok.Data;

/**
 * @author: junjiexun
 * @date: 2020/10/13 10:05 下午
 * @description:
 */

@Data
public abstract class RedisObject<T> implements Snapshot {

    private ObjectType type;

    private T original;

    public boolean isEmptyValue() {
        return this.original == null;
    }

}
