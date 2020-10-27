package ginyu.persist;

import ginyu.core.Server;

public interface Saver {

    void save();

    default void load() {
        load(null);
    }

    void load(String filePath);
}
