package ginyu.object;

/**
 * @author: junjiexun
 * @date: 2020/10/13 10:48 下午
 * @description:
 */
public class ListObject extends RedisObject<List> {

    public ListObject() {
        this.setType(ObjectType.LIST);
        this.setOriginal(new List());
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
