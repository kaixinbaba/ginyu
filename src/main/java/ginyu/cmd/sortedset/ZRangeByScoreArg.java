package ginyu.cmd.sortedset;

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
public class ZRangeByScoreArg extends ZScoreRangeArg {

    private Boolean withScores;

    private Boolean limit;

    private Integer offset;

    private Integer count;
}
