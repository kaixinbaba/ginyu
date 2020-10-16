package cmd.set;

import lombok.Data;

/**
 * @author: junjiexun
 * @date: 2020/10/13 2:00 下午
 * @description:
 */
@Data
public class SInterArg {

    private String[] keys;

    public SInterArg(String... keys) {
        this.keys = keys;
    }
}
