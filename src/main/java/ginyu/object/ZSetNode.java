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
public class ZSetNode implements Comparable<ZSetNode> {

    private String member;

    private Double score;

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ZSetNode)) {
            return false;
        }
        return this.member.equals(((ZSetNode) obj).member);
    }

    @Override
    public int hashCode() {
        return this.member.hashCode();
    }

    @Override
    public int compareTo(ZSetNode node) {
        int compareResult = this.score.compareTo(node.score);
        if (compareResult == 0) {
            return this.member.compareTo(node.member);
        } else {
            return compareResult;
        }
    }
}
