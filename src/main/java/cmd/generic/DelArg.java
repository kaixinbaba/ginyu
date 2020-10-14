package cmd.generic;

import lombok.Data;

/**
 * @author: junjiexun
 * @date: 2020/10/13 2:00 下午
 * @description:
 */
@Data
public class DelArg {

    public DelArg(String... keys) {
        this.keys = keys;
    }

    private String[] keys;
}
