package ginyu.core;

import ginyu.common.Consoles;
import ginyu.config.GinyuConfig;
import ginyu.db.Db;
import ginyu.io.Communicator;
import ginyu.io.NettyCommunicator;
import ginyu.task.CleanExpiredTask;
import ginyu.task.WakeupTimeoutClientTask;
import ginyu.utils.ConfigUtils;
import lombok.Getter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: junjiexun
 * @date: 2020/10/13 9:53 下午
 * @description:
 */
@SuppressWarnings("all")
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

    private WakeupTimeoutClientTask wakeupTimeoutClientTask;

    private Server() {
    }

    public void init(String[] args) {
        this.ginyuConfig = ConfigUtils.getConfig(args);
        this.communicator = new NettyCommunicator();
        this.db = new Db(this.ginyuConfig.getDbSize());
        this.cleanExpiredTask = new CleanExpiredTask();
        this.wakeupTimeoutClientTask = new WakeupTimeoutClientTask();
    }

    private void printEnv() {
        Consoles.blue("OS info          : {} {} {}",
                System.getProperty("os.name").toLowerCase(Locale.ENGLISH),
                System.getProperty("os.version").toLowerCase(Locale.ENGLISH),
                System.getProperty("os.arch").toLowerCase(Locale.ENGLISH)
        );
        Consoles.blue("JAVA version     : {}", System.getProperty("java.version").toLowerCase(Locale.ENGLISH));
        Consoles.blue("JAVA home        : {}", System.getProperty("java.home").toLowerCase(Locale.ENGLISH));
    }

    private void printBanner() throws IOException {
        File f = new File("banner.txt");
        Consoles.magenta(FileUtils.readFileToString(f, Charset.forName("UTF-8")));
    }

    private void printGinyuConfig() {
        Consoles.blue("GINYU port       : {}", ginyuConfig.getPort());
        Consoles.blue("GINYU logLevel   : {}", ginyuConfig.getLogLevel());
        Consoles.blue("GINYU dbSize     : {}", ginyuConfig.getDbSize());
        System.out.println();
    }

    public void start() throws Exception {
        printBanner();
        printEnv();
        printGinyuConfig();
        Consoles.info("start listening port {}", ginyuConfig.getPort());
        this.communicator.start(ginyuConfig);
        this.startTask();
        Consoles.info("server started");
    }

    public void addClient(Client client) {
        this.clients.put(client.getId(), client);
    }

    public void removeClient(Client client) {
        this.clients.remove(client.getId());
    }

    private void startTask() {
        this.cleanExpiredTask.start();
        this.wakeupTimeoutClientTask.start();
    }
}
