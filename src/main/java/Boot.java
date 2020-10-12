import config.GinyuConfig;
import io.Communicator;
import io.NettyCommunicator;

/**
 * @author: junjiexun
 * @date: 2020/10/10 10:40 下午
 * @description: 启动类
 */
public class Boot {

    public static void main(String[] args) {
        GinyuConfig ginyuConfig = new GinyuConfig();
        ginyuConfig.setPort(9736);
        Communicator communicator = new NettyCommunicator();
        communicator.start(ginyuConfig);
        System.out.println("main end");
    }
}
