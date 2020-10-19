package cmd.list;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: junjiexun
 * @date: 2020/10/19 4:26 下午
 * @description:
 */
@Data
@AllArgsConstructor
public class BPopArg {

    private String[] keys;

    private Integer timeout;
}
