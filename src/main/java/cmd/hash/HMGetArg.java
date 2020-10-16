package cmd.hash;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: junjiexun
 * @date: 2020/10/16 10:41 上午
 * @description:
 */
@Data
@AllArgsConstructor
public class HMGetArg {

    private String key;

    private String[] fields;
}
