import core.Server;

/**
 * @author: junjiexun
 * @date: 2020/10/10 10:40 下午
 * @description: 启动类
 */
public class Boot {

    public static void main(String[] args) {
        Server server = Server.INSTANCE;
        server.init(args);
        server.start();
    }
}
