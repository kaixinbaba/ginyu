import common.Consoles;
import core.Server;

/**
 * @author: junjiexun
 * @date: 2020/10/10 10:40 下午
 * @description: 启动类
 */
public class Boot {

    public static void main(String[] args) {
        try {
            Server server = Server.INSTANCE;
            server.init(args);
            server.start();
        } catch (Exception e) {
            Consoles.error("ginyu startup failed! cause on {}", e.getMessage());
            System.exit(1);
        }
    }
}
