package ginyu.object;

import ginyu.protocol.BulkStrings;
import ginyu.protocol.Integers;
import ginyu.protocol.Resp2;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author: junjiexun
 * @date: 2020/10/16 3:18 下午
 * @description:
 */
@SuppressWarnings("all")
public class ZSet {

    private final ConcurrentHashMap<String, Double> members = new ConcurrentHashMap<>();

    private final ConcurrentSkipListSet<ZSetNode> nodes = new ConcurrentSkipListSet<>();


    public int size() {
        return this.nodes.size();
    }

    private ZSetNode getCorrectScore(ZSetNode node, Boolean incr) {
        Double currentScore = this.members.get(node.getMember());
        currentScore = currentScore == null ? 0.0D : currentScore;
        Double score = incr ? node.getScore() + currentScore : node.getScore();
        node.setScore(score);
        return node;
    }

    public Resp2 add(Boolean ch, Boolean incr, ZSetNode... nodes) {
        Integer result = 0;
        for (ZSetNode node : nodes) {
            node = getCorrectScore(node, incr);
            this.nodes.add(node);
            Double preValue = this.members.put(node.getMember(), node.getScore());
            if (incr) {
                return BulkStrings.create(String.valueOf(node.getScore()));
            }
            if (ch) {
                if (preValue != null && !preValue.equals(node.getScore())) {
                    result++;
                }
            } else {
                if (preValue == null) {
                    result++;
                }
            }
        }
        return Integers.create(result);
    }
}
