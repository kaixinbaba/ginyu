package cmd.generic;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: junjiexun
 * @date: 2020/10/13 2:00 下午
 * @description:
 */
@Data
@AllArgsConstructor
public class PExpireArg {

    private String key;

    private Long milliseconds;
}
