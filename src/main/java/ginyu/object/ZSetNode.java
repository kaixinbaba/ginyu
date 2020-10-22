package ginyu.object;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: junjiexun
 * @date: 2020/10/21 9:49 下午
 * @description:
 */
@Data
@AllArgsConstructor
public class ZSetNode {

    private String member;

    private Double score;
}
