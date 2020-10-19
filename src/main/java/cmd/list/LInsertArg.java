package cmd.list;

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
public class LInsertArg extends KeyArg {

    private Boolean isBefore;

    private String pivot;

    private String value;

    public LInsertArg(String key, Boolean isBefore, String pivot, String value) {
        super(key);
        this.isBefore = isBefore;
        this.pivot = pivot;
        this.value = value;
    }
}
