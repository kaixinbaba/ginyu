package ginyu.cmd.hash;

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
public class HSetArg extends KeyArg {

    private HashEntry[] entries;

    public HSetArg(String key, HashEntry... entries) {
        super(key);
        this.entries = entries;
    }

}
