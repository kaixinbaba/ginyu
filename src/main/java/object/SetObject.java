package object;

/**
 * @author: junjiexun
 * @date: 2020/10/13 10:48 下午
 * @description:
 */
public class SetObject extends RedisObject<Dict<String, None>> {

    public SetObject() {
        this.setType(ObjectType.SET);
        this.setOriginal(new Dict<>());
    }
}
