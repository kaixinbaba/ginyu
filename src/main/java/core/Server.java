package core;

import config.GinyuConfig;
import db.Db;
import io.Communicator;
import io.NettyCommunicator;
import lombok.Getter;
import utils.ConfigUtils;

/**
 * @author: junjiexun
 * @date: 2020/10/13 9:53 下午
 * @description:
 */
public class Server {

    @Getter
    private Communicator communicator;

    @Getter
    private GinyuConfig ginyuConfig;

    @Getter
    private Db db;

    public static final Server INSTANCE = new Server();

    private Server() {
    }

    public void init(String[] args) {
        this.ginyuConfig = ConfigUtils.getConfig(args);
        this.communicator = new NettyCommunicator();
        this.db = new Db(this.ginyuConfig.getDbSize());
    }

    public void start() {
        this.communicator.start(ginyuConfig);
        System.out.println("server started");
    }
}
