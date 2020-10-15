package object;

/**
 * @author: junjiexun
 * @date: 2020/10/13 10:48 下午
 * @description:
 */
public class HashObject extends RedisObject<Dict<String, String>> {

    public HashObject() {
        this.setType(ObjectType.HASH);
        this.setOriginal(new Dict<>());
    }
}
