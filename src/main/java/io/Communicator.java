package io;

import config.GinyuConfig;

public interface Communicator {

    void start(GinyuConfig ginyuConfig);

    void close();
}
