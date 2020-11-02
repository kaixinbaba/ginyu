package ginyu.object;

import ginyu.core.Snapshot;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: junjiexun
 * @date: 2020/10/13 10:10 下午
 * @description: TODO redis底层双数组
 */
public class Dict<K, V> extends ConcurrentHashMap<K, V> implements Snapshot {

    public Dict() {
    }

    public Dict(Dict<K, V> dict) {
        super(dict);
    }

    @Override
    public String toSnapshot() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.size());
        for (Entry<K, V> kvEntry : this.entrySet()) {
            // TODO 如果没有实现Snapshot的话 用toString肯定有问题的, key的长度也要存下来
            if (kvEntry.getKey() instanceof Snapshot) {
                sb.append(((Snapshot) kvEntry.getKey()).toSnapshot());
            } else {
                sb.append(kvEntry.getKey().toString());
            }
            if (kvEntry.getValue() instanceof Snapshot) {
                sb.append(((Snapshot) kvEntry.getValue()).toSnapshot());
            } else {
                sb.append(kvEntry.getValue().toString());
            }
        }
        return sb.toString();
    }
}
