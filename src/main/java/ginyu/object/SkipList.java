package ginyu.object;

import ginyu.cmd.sortedset.ZScoreRangeArg;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ginyu.common.Constants.SLOGAN;

/**
 * @author: junjiexun
 * @date: 2020/10/22 10:35 上午
 * @description:
 */
@SuppressWarnings("all")
public class SkipList {

    private static final int LUCK = 0;
    private final SkipListNode HEAD;
    private volatile Integer length;
    private volatile int level;
    private volatile SkipListNode TAIL = null;

    public SkipList() {
        this.HEAD = new SkipListNode(SLOGAN, Double.MIN_VALUE);
        this.level = 1;
        this.length = 0;
    }

    public Integer size() {
        return this.length;
    }

    private int randomLevel() {
        // 和redis一样 4分之1的概率 升级
        int level = 1;
        // 0, 1, 2, 3
        while (RandomUtils.nextInt(0, 4) == LUCK) {
            level++;
        }
        return level;
    }

    public synchronized void add(ZSetNode zSetNode, ZSet zSet) {
        this.add(zSetNode.getMember(), zSetNode.getScore(), zSet);
    }

    /**
     * TODO 如何保证并发安全
     *
     * @param member
     * @param score
     */
    public synchronized void add(String member, Double score, ZSet zSet) {
        // TODO maxLevel in config
        SkipListNode[] update = new SkipListNode[32];
        SkipListNode node = this.HEAD;
        // 从最高level开始遍历
        for (int i = this.level - 1; i >= 0; i--) {
            // 这里的get会不会越界
            SkipListLevel skipListLevel = node.getLevel()[i];
            // 首先得有下一个节点
            while (node.getLevel()[i].getNext() != null
                    // 当前分数更大
                    && (node.getLevel()[i].getNext().getScore() < score
                    // 或者分数一样，字符串更大
                    || (node.getLevel()[i].getNext().getScore().equals(score))
                    && node.getLevel()[i].getNext().getMember().compareTo(member) < 0)) {
                // 说明当前新的节点应该排在这个next之后
                node = node.getLevel()[i].getNext();
            }
            update[i] = node;
        }
        int level = this.randomLevel();
        if (level > this.level) {
            for (int i = this.level; i < level; i++) {
                update[i] = this.HEAD;
            }
            this.level = level;
        }
        node = new SkipListNode(member, score, level);
        for (int i = 0; i < level; i++) {
            node.getLevel()[i].setNext(update[i].getLevel()[i].getNext());
            update[i].getLevel()[i].setNext(node);
        }
        node.setPrev(update[0] == this.HEAD ? null : update[0]);
        if (node.getLevel()[0].getNext() != null) {
            node.getLevel()[0].getNext().setPrev(node);
        } else {
            this.TAIL = node;
        }
        this.length++;
        // 同时更新字典
        zSet.put(member, score);
    }

    public synchronized void update(ZSetNode node, Double currentScore, ZSet zSet) {
        this.update(node.getMember(), node.getScore(), currentScore, zSet);
    }

    public synchronized void update(String member, Double newScore, Double currentScore, ZSet zSet) {
        // TODO maxLevel in config
        SkipListNode[] update = new SkipListNode[32];
        SkipListNode node = this.HEAD;
        // 从最高level开始遍历
        for (int i = this.level - 1; i >= 0; i--) {
            // 首先得有下一个节点
            while (node.getLevel()[i].getNext() != null
                    // 当前分数更大
                    && (node.getLevel()[i].getNext().getScore() < currentScore
                    // 或者分数一样，字符串更大
                    || (node.getLevel()[i].getNext().getScore().equals(currentScore))
                    && node.getLevel()[i].getNext().getMember().compareTo(member) < 0)) {
                // 说明当前新的节点应该排在这个next之后
                node = node.getLevel()[i].getNext();
            }
            update[i] = node;
        }
        node = node.getLevel()[0].getNext();
        // 新分数小于前一个节点的分数
        if ((node.getPrev() == null || node.getPrev().getScore() < newScore)
                // 新分数大于后一个节点的分数
                && (node.getLevel()[0].getNext() == null || node.getLevel()[0].getNext().getScore() > newScore)) {
            // 如果新的分数在原有前后节点分数区间内，直接更新当前节点的分数即可
            node.setScore(newScore);
            // 同时更新字典
            zSet.updateScore(member, newScore);
            return;
        }
        this.delete(node, update, zSet);
        // 重新插入新的节点
        this.add(member, newScore, zSet);
    }

    private void delete(SkipListNode node, SkipListNode[] update, ZSet zSet) {
        for (int i = 0; i < this.level; i++) {
            if (update[i].getLevel()[i].getNext() == node) {
                update[i].getLevel()[i].setNext(node.getLevel()[i].getNext());
            }
        }
        if (node.getLevel()[0].getNext() != null) {
            node.getLevel()[0].getNext().setPrev(node.getPrev());
        } else {
            this.TAIL = node.getPrev();
        }
        // 从最高层开始检查如果HEAD之后就是null的话，就降层
        while (this.level > 1 && this.HEAD.getLevel()[this.level - 1].getNext() == null) {
            this.level--;
        }
        this.length--;
        zSet.remove(node.getMember());
    }

    public Long countByScoreRange(ZScoreRangeArg arg) {
        return this.getNodesByScoreRange(arg, false, null, null).stream().count();
    }

    public List<Double> getScoresByScoreRange(ZScoreRangeArg arg) {
        return this.getNodesByScoreRange(arg, false, null, null)
                .stream()
                .map(ZSetNode::getScore)
                .collect(Collectors.toList());
    }

    public List<String> getMembersByScoreRange(ZScoreRangeArg arg,
                                               Boolean withScores,
                                               Integer offset, Integer count) {
        return this.getNodesByScoreRange(arg, withScores, offset, count)
                .stream()
                .map(ZSetNode::getMember)
                .collect(Collectors.toList());
    }

    public List<ZSetNode> getNodesByScoreRange(ZScoreRangeArg arg,
                                               Boolean withScores,
                                               Integer offset, Integer count) {

        List<ZSetNode> nodes = new ArrayList<>();
        SkipListNode node = this.HEAD.getLevel()[0].getNext();
        int i = 0;
        while (node != null) {
            if (offset != null && i < offset) {
            } else if (((arg.getOpenIntervalMin() && node.getScore() > arg.getMin())
                    || (!arg.getOpenIntervalMin() && node.getScore() >= arg.getMin()))
                    && ((arg.getOpenIntervalMax() && node.getScore() < arg.getMax())
                    || (!arg.getOpenIntervalMax() && node.getScore() <= arg.getMax()))) {
                nodes.add(new ZSetNode(node.getMember(), node.getScore()));
            } else if ((arg.getOpenIntervalMax() && node.getScore() >= arg.getMax())
                    || node.getScore() > arg.getMax()) {
                break;
            }
            if (count != null && nodes.size() >= count) {
                return nodes;
            }
            node = node.getLevel()[0].getNext();
            i++;
        }
        return nodes;
    }

    public List<ZSetNode> getNodesByIndexRange(Integer start, Integer stop) {
        List<ZSetNode> nodes = new ArrayList<>();
        SkipListNode node = this.HEAD.getLevel()[0].getNext();
        int i = 0;
        while (node != null) {
            if (i >= start && i <= stop) {
                nodes.add(new ZSetNode(node.getMember(), node.getScore()));
            } else if (i > stop) {
                break;
            }
            node = node.getLevel()[0].getNext();
            i++;
        }
        return nodes;
    }

    public List<String> getMembersByIndexRange(Integer start, Integer stop) {
        return this.getNodesByIndexRange(start, stop)
                .stream()
                .map(ZSetNode::getMember)
                .collect(Collectors.toList());
    }
}
