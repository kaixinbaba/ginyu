package utils;

import config.GinyuConfig;

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
        return ginyuConfig;
    }
}
