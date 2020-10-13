package cmd.string;

import lombok.Data;

/**
 * @author: junjiexun
 * @date: 2020/10/13 2:00 下午
 * @description:
 */
@Data
public class SetArg {

    private String key;

    private String value;

    private Long expiredMilliSeconds;

    private Boolean xx;

    private Boolean nx;

}
