package cmd.list;

import cmd.Command;


/**
 * @author: junjiexun
 * @date: 2020/10/16 10:41 上午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "lpushx")
public class LPushX extends PushX {

    @Override
    protected boolean isLeft() {
        return true;
    }
}
