package ginyu.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: junjiexun
 * @date: 2020/10/11 9:28 下午
 * @description:
 */
@Data
@AllArgsConstructor
public class BulkString {

    private Integer length;

    private String content;
}
