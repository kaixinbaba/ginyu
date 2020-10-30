package ginyu.persist;

import java.io.IOException;

public interface Saver {

    void save();

    default void tryLoad() {
        tryLoad(null);
    }

    default void tryLoad(String filePath) {
        try {
            load(filePath);
        } catch (IOException e) {
            // ignore
        }
    }

    default void load() throws IOException {
        load(null);
    }

    void load(String filePath) throws IOException;
}
