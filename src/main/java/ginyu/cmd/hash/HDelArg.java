package ginyu.cmd.hash;

import ginyu.cmd.KeyArg;
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
public class HDelArg extends KeyArg {

    private String[] fields;

    public HDelArg(String key, String... fields) {
        super(key);
        this.fields = fields;
    }
}
