package db;

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

    public Database getDatabase(Integer id) {
        return this.databases.get(id);
    }

}
