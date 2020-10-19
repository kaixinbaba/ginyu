package ginyu.object;

import ginyu.cmd.sortedset.ScoreMember;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ConcurrentHashMap;

import static ginyu.common.Constants.SLOGAN;

/**
 * @author: junjiexun
 * @date: 2020/10/16 3:18 下午
 * @description:
 */
@SuppressWarnings("all")
public class ZSet {

    private final ZSetNode HEAD = new ZSetNode(Double.MIN_VALUE, SLOGAN, null, null);
    private ConcurrentHashMap<String, Double> members = new ConcurrentHashMap<>();
    private volatile ZSetNode TAIL;

    public boolean exists(String member) {
        return this.members.containsKey(member);
    }

    public void addNodes(ScoreMember[] scoreMembers, Boolean xx, Boolean nx, Boolean incr, Boolean ch) {
        ZSetNode node = this.HEAD.next;
        while (node != null) {

        }
    }

    @AllArgsConstructor
    static class ZSetNode implements Comparable<ZSetNode> {

        Double score;

        String member;

        @Getter
        @Setter
        ZSetNode prev;

        @Setter
        @Getter
        ZSetNode next;

        @Override
        public int hashCode() {
            return score.hashCode() + member.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof ZSetNode)) {
                return false;
            }
            ZSetNode other = (ZSetNode) obj;
            return this.score.equals(other.score) && this.member.equals(other.member);
        }

        @Override
        public int compareTo(ZSetNode o) {
            if (this.score.equals(o.score)) {
                return this.member.compareTo(o.member);
            }
            return this.score.compareTo(o.score);
        }
    }
}
