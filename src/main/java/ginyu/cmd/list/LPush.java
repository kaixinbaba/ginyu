package ginyu.cmd.list;

import ginyu.cmd.Command;


/**
 * @author: junjiexun
 * @date: 2020/10/16 10:41 上午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "lpush")
public class LPush extends Push {

    @Override
    protected boolean isLeft() {
        return true;
    }
}
