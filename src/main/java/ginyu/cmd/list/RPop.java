package ginyu.cmd.list;

import ginyu.cmd.Command;

/**
 * @author: junjiexun
 * @date: 2020/10/13 1:56 下午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "rpop")
public class RPop extends Pop {
    @Override
    protected boolean isLeft() {
        return false;
    }
}
