package ginyu.object;

import ginyu.protocol.BulkStrings;
import ginyu.protocol.Integers;
import ginyu.protocol.Resp2;

/**
 * @author: junjiexun
 * @date: 2020/10/16 3:18 下午
 * @description:
 */
@SuppressWarnings("all")
public class ZSet {

    private final Dict<String, Double> members = new Dict<>();

    private final SkipList skipList = new SkipList();

    public int size() {
        return this.skipList.size();
    }

    private ZSetNode getCorrectScore(ZSetNode node, Boolean incr) {
        Double currentScore = this.members.get(node.getMember());
        currentScore = currentScore == null ? 0.0D : currentScore;
        Double score = incr ? node.getScore() + currentScore : node.getScore();
        node.setScore(score);
        return node;
    }

    private Integer updateResult(Integer oldResult, Boolean ch, Double preValue, Double currentValue) {
        if (ch) {
            if (preValue != null && !preValue.equals(currentValue)) {
                oldResult++;
            }
        } else {
            if (preValue == null) {
                oldResult++;
            }
        }
        return oldResult;
    }

    public Resp2 add(Boolean ch, Boolean incr, ZSetNode... nodes) {
        Integer result = 0;
        for (ZSetNode node : nodes) {
            node = getCorrectScore(node, incr);
            Double currentScore = this.members.get(node.getMember());
            if (currentScore == null) {
                // 新增
                this.skipList.add(node, this);
            } else {
                if (!currentScore.equals(node.getScore())) {
                    // 修改
                    this.skipList.update(node, currentScore, this);
                }
            }
            if (incr) {
                return BulkStrings.create(String.valueOf(node.getScore()));
            }
            result = updateResult(result, ch, currentScore, node.getScore());
        }
        return Integers.create(result);
    }

    public void updateScore(String member, Double score) {
        this.members.put(member, score);
    }

    public void put(String member, Double score) {
        this.members.put(member, score);
    }

    public void remove(String member) {
        this.members.remove(member);
    }
}
