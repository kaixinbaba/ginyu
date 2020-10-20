package ginyu.object;

/**
 * @author: junjiexun
 * @date: 2020/10/13 10:48 下午
 * @description:
 */
public class SetObject extends RedisObject<Dict<String, SetObject.None>> {

    public static final None NONE = new None();

    public SetObject() {
        this.setType(ObjectType.SET);
        this.setOriginal(new Dict<>());
    }

    @Override
    public boolean isEmptyValue() {
        return super.isEmptyValue() || this.getOriginal().isEmpty();
    }

    public static class None {
        private None() {
        }
    }
}
