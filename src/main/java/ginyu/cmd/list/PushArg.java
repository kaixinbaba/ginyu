package ginyu.cmd.list;

import ginyu.cmd.KeyArg;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author: junjiexun
 * @date: 2020/10/16 10:41 上午
 * @description:
 */
@Data
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PushArg extends KeyArg {

    private String[] values;

    public PushArg(String key, String... values) {
        super(key);
        this.values = values;
    }
}
