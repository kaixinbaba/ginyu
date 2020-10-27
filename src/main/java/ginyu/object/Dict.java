package ginyu.object;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: junjiexun
 * @date: 2020/10/13 10:10 下午
 * @description: TODO redis底层双数组
 */
public class Dict<K, V> extends ConcurrentHashMap<K, V> {

    public Dict() {
    }

    public Dict(Dict<K, V> dict) {
        super(dict);
    }
}
