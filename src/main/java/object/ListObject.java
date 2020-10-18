package object;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author: junjiexun
 * @date: 2020/10/13 10:48 下午
 * @description:
 */
public class ListObject extends RedisObject<LinkedBlockingDeque<String>> {

    public ListObject() {
        this.setType(ObjectType.LIST);
        this.setOriginal(new LinkedBlockingDeque<>());
    }
}
