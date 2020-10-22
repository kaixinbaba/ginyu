package ginyu.cmd.sortedset;

import ginyu.cmd.KeyArg;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author: junjiexun
 * @date: 2020/10/16 2:54 下午
 * @description:
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ZCountArg extends KeyArg {

    private Double min;

    private Double max;

    public ZCountArg(String key, Double min, Double max) {
        super(key);
        this.min = min;
        this.max = max;
    }

}
