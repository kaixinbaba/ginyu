package ginyu.cmd.list;

import ginyu.cmd.KeyArg;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author: junjiexun
 * @date: 2020/10/13 2:00 下午
 * @description:
 */
@Data
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class LRangeArg extends KeyArg {

    private Integer start;

    private Integer stop;

    public LRangeArg(String key, Integer start, Integer stop) {
        super(key);
        this.start = start;
        this.stop = stop;
    }
}
