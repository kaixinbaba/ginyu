package ginyu.cmd.string;

import ginyu.cmd.KeyArg;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author: junjiexun
 * @date: 2020/10/26 6:45 下午
 * @description:
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CrementByArg extends KeyArg {

    private Integer crement;

    public CrementByArg(String key, Integer crement) {
        super(key);
        this.crement = crement;
    }

}
