package cmd.string;

import cmd.KeyArg;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author: junjiexun
 * @date: 2020/10/13 2:00 下午
 * @description:
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SetArg extends KeyArg {

    private String value;

    private Long expiredMilliSeconds;

    private Boolean xx;

    private Boolean nx;

}
