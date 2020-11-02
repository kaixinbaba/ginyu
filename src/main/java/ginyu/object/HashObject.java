package ginyu.object;

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

    @Override
    public boolean isEmptyValue() {
        return super.isEmptyValue() || this.getOriginal().isEmpty();
    }

    @Override
    public String toSnapshot() {
        return null;
    }
}
