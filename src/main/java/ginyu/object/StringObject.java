package ginyu.object;

/**
 * @author: junjiexun
 * @date: 2020/10/13 10:48 下午
 * @description:
 */
public class StringObject extends RedisObject<GinyuString> {

    public StringObject() {
        this(null);
    }

    public StringObject(String value) {
        this.setType(ObjectType.STRING);
        this.setOriginal(new GinyuString(value));
    }
}
