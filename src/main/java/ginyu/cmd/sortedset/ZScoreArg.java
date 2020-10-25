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
public class ZScoreArg extends KeyArg {

    private String member;

    public ZScoreArg(String key, String member) {
        super(key);
        this.member = member;
    }
}
