package ginyu.cmd.set;

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
public class SPopArg extends KeyArg {

    private Integer count;

    public SPopArg(String key, Integer count) {
        super(key);
        this.count = count;
    }
}
