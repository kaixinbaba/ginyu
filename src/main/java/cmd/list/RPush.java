package cmd.list;

import cmd.Command;


/**
 * @author: junjiexun
 * @date: 2020/10/16 10:41 上午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "rpush")
public class RPush extends Push {

    @Override
    protected boolean isLeft() {
        return false;
    }
}
