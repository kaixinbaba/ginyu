package ginyu.io;

import ginyu.config.GinyuConfig;

public interface Communicator {

    void start(GinyuConfig ginyuConfig);

    void close();
}
