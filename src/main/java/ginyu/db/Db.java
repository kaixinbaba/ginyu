package ginyu.db;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: junjiexun
 * @date: 2020/10/13 10:03 下午
 * @description:
 */
public class Db {

    @Getter
    private final List<Database> databases;

    public Db(Integer dbSize) {
        this.databases = new ArrayList<>(dbSize);
        for (int i = 0; i < dbSize; i++) {
            this.databases.add(new Database(i));
        }
    }

    public Db(Db db) {
        List<Database> databases = db.getDatabases();
        this.databases = new ArrayList<>(databases.size());
        for (Database database : databases) {
            this.databases.add(new Database(database));
        }
    }

    public Database getDatabase(Integer id) {
        return this.databases.get(id);
    }

    public void swapDb(Integer index1, Integer index2) {
        synchronized (this.databases) {
            Database database1 = databases.get(index1);
            Database database2 = databases.get(index2);
            databases.set(index1, database2);
            databases.set(index2, database1);
        }
    }
}
