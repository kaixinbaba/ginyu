package cmd.generic;

import cmd.KeyArg;
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
public class ExpireArg extends KeyArg {

    private Integer seconds;

    public ExpireArg(String key, Integer seconds) {
        super(key);
        this.seconds = seconds;
    }
}
