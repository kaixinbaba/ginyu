package cmd.hash;

import cmd.KeyArg;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author: junjiexun
 * @date: 2020/10/16 10:41 上午
 * @description:
 */
@Data
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class HMGetArg extends KeyArg {

    private String[] fields;

    public HMGetArg(String key, String... fields) {
        super(key);
        this.fields = fields;
    }
}
