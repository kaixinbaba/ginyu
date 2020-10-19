package ginyu.cmd.generic;

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
public class RenameArg extends KeyArg {

    private String newKey;

    public RenameArg(String key, String newKey) {
        super(key);
        this.newKey = newKey;
    }
}
