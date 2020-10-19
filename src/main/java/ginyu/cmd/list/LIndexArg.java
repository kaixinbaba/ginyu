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
public class LIndexArg extends KeyArg {

    private Integer index;

    public LIndexArg(String key, Integer index) {
        super(key);
        this.index = index;
    }
}
