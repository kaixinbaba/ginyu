package cmd.sortedset;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: junjiexun
 * @date: 2020/10/16 2:54 下午
 * @description:
 */
@Data
@AllArgsConstructor
public class ZAddArg {

    private String key;

    private Boolean xx;

    private Boolean nx;

    private Boolean incr;

    private Boolean ch;

    private ScoreMember[] scoreMembers;

}
