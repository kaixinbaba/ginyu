package ginyu.cmd.sortedset;

import ginyu.cmd.KeyArg;
import ginyu.object.ZSetNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author: junjiexun
 * @date: 2020/10/16 2:54 下午
 * @description:
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ZAddArg extends KeyArg {

    private Boolean xx;

    private Boolean nx;

    private Boolean incr;

    private Boolean ch;

    private ZSetNode[] nodes;

}
