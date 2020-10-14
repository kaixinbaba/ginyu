package core;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: junjiexun
 * @date: 2020/10/14 3:22 下午
 * @description:
 */
public abstract class ClientFactory {

    private static final AtomicInteger CLIENT_ID_CURSOR = new AtomicInteger(1);

    public static Client createClient() {
        Client client = new Client();
        client.setId(CLIENT_ID_CURSOR.getAndIncrement());
        // default to db 0
        client.setDb(0);
        return client;
    }
}
