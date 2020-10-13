package object;

import lombok.Data;

/**
 * @author: junjiexun
 * @date: 2020/10/13 10:05 下午
 * @description:
 */
@Data
public abstract class RedisObject<T> {

    private ObjectType type;

    private T original;

}
