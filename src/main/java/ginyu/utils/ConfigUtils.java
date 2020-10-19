package ginyu.utils;

import ginyu.common.Consoles;
import ginyu.config.GinyuConfig;

import java.util.logging.Level;

/**
 * @author: junjiexun
 * @date: 2020/10/13 9:55 下午
 * @description:
 */
public abstract class ConfigUtils {

    public static GinyuConfig getConfig(String[] args) {
        GinyuConfig ginyuConfig = new GinyuConfig();
        ginyuConfig.setPort(9736);
        ginyuConfig.setDbSize(16);
        ginyuConfig.setLogLevel(0);
        switch (ginyuConfig.getLogLevel()) {
            case 0:
                Consoles.LEVEL = Level.FINE;
                break;
            case 1:
                Consoles.LEVEL = Level.CONFIG;
                break;
            case 2:
                Consoles.LEVEL = Level.INFO;
                break;
            case 3:
                Consoles.LEVEL = Level.WARNING;
                break;
            case 4:
                Consoles.LEVEL = Level.SEVERE;
                break;
            default:
                throw new IllegalArgumentException();
        }
        return ginyuConfig;
    }
}
