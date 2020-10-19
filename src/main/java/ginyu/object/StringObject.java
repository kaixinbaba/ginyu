package ginyu.object;

/**
 * @author: junjiexun
 * @date: 2020/10/13 10:48 下午
 * @description:
 */
public class StringObject extends RedisObject<String> {

    public StringObject() {
        this.setType(ObjectType.STRING);
    }
}
