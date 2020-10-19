package ginyu.cmd.list;

import ginyu.cmd.Command;

/**
 * @author: junjiexun
 * @date: 2020/10/13 1:56 下午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "brpop")
public class BRPop extends BPop {
    @Override
    protected boolean isLeft() {
        return false;
    }
}
