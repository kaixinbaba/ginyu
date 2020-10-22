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
public class ZRangeArg extends KeyArg {

    private Integer start;

    private Integer stop;

    private Boolean withScores;

    public ZRangeArg(String key, Integer start, Integer stop, Boolean withScores) {
        super(key);
        this.start = start;
        this.stop = stop;
        this.withScores = withScores;
    }

}
