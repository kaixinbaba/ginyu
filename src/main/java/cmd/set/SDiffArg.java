package cmd.set;

import lombok.Data;

/**
 * @author: junjiexun
 * @date: 2020/10/13 2:00 下午
 * @description:
 */
@Data
public class SDiffArg {

    private String[] keys;

    public SDiffArg(String... keys) {
        this.keys = keys;
    }
}
