package ginyu.cmd.list;

import ginyu.cmd.Command;

/**
 * @author: junjiexun
 * @date: 2020/10/13 1:56 下午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "lpop", checkExpire = true)
public class LPop extends Pop {
    @Override
    protected boolean isLeft() {
        return true;
    }
}
