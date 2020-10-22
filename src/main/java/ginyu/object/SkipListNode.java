package ginyu.object;

import lombok.Data;

/**
 * @author: junjiexun
 * @date: 2020/10/22 10:46 上午
 * @description:
 */
@Data
@SuppressWarnings("all")
public class SkipListNode {

    private final String member;

    private volatile Double score;

    private SkipListNode prev;

    private final SkipListLevel[] level;

    public SkipListNode(String member, Double score) {
        // TODO maxLevel from config
        this(member, score, 32);
    }

    public SkipListNode(String member, Double score, Integer initLevel) {
        this.member = member;
        this.score = score;
        this.level = new SkipListLevel[initLevel];
        for (int i = 0; i < initLevel; i++) {
            this.level[i] = new SkipListLevel();
        }
    }

}
