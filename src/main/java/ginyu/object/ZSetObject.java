package ginyu.object;

/**
 * @author: junjiexun
 * @date: 2020/10/13 10:48 下午
 * @description:
 */
public class ZSetObject extends RedisObject<ZSet> {

    public ZSetObject() {
        this.setType(ObjectType.ZSET);
        this.setOriginal(new ZSet());
    }
}
