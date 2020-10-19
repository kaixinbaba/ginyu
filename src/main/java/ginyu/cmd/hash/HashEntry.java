package ginyu.cmd.hash;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: junjiexun
 * @date: 2020/10/15 10:14 下午
 * @description:
 */
@Data
@AllArgsConstructor
public class HashEntry {

    private String field;

    private String value;
}
