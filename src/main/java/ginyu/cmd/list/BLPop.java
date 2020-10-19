package ginyu.cmd.list;

import ginyu.cmd.Command;

/**
 * @author: junjiexun
 * @date: 2020/10/13 1:56 下午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "blpop")
public class BLPop extends BPop {
    @Override
    protected boolean isLeft() {
        return true;
    }
}
