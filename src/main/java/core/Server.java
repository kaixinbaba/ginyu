package core;

import config.GinyuConfig;
import db.Db;
import io.Communicator;
import io.NettyCommunicator;
import lombok.Getter;
import task.CleanExpiredTask;
import utils.ConfigUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: junjiexun
 * @date: 2020/10/13 9:53 下午
 * @description:
 */
public class Server {

    public static final Server INSTANCE = new Server();
    @Getter
    private final Map<Integer, Client> clients = new ConcurrentHashMap<>();
    @Getter
    private Communicator communicator;
    @Getter
    private GinyuConfig ginyuConfig;
    @Getter
    private Db db;

    private CleanExpiredTask cleanExpiredTask;

    private Server() {
    }

    public void init(String[] args) {
        this.ginyuConfig = ConfigUtils.getConfig(args);
        this.communicator = new NettyCommunicator();
        this.db = new Db(this.ginyuConfig.getDbSize());
        this.cleanExpiredTask = new CleanExpiredTask();
    }

    public void start() {
        this.communicator.start(ginyuConfig);
        this.startTask();
        System.out.println("server started");
    }

    public void addClient(Client client) {
        this.clients.put(client.getId(), client);
    }

    public void removeClient(Client client) {
        this.clients.remove(client.getId());
    }

    private void startTask() {
        this.cleanExpiredTask.start();
    }
}
