package ginyu.persist;

import ginyu.core.Snapshot;
import ginyu.db.Db;
import lombok.Data;

/**
 * @author: junjiexun
 * @date: 2020/10/27 9:48 下午
 * @description:
 */
@Data
public class ServerForSaver implements Snapshot {

    private Db db;

    @Override
    public String toSnapshot() {
        return this.db.toSnapshot();
    }
}
