package ginyu.cmd.sortedset;

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
public class ZIncrByArg extends KeyArg {

    private Double increment;

    private String member;

    public ZIncrByArg(String key, Double increment, String member) {
        super(key);
        this.increment = increment;
        this.member = member;
    }
}
