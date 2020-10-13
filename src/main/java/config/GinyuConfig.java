package config;

import lombok.Data;

/**
 * @author: junjiexun
 * @date: 2020/10/11 8:20 下午
 * @description:
 */
@Data
public class GinyuConfig {

    private Integer port;

    private Integer logLevel;

    private Integer dbSize;

}
