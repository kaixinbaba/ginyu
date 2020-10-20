package ginyu.event.list;

import ginyu.cmd.list.BPopArg;
import ginyu.core.Client;
import ginyu.event.BlockEvent;
import lombok.Data;

/**
 * @author: junjiexun
 * @date: 2020/10/20 11:48 上午
 * @description:
 */
@Data
@SuppressWarnings("all")
public class BlockByBPopEvent extends BlockEvent {

    private BPopArg arg;

    private Boolean isLeft;

    public BlockByBPopEvent(Client client, BPopArg arg, Boolean isLeft) {
        super(client);
        this.arg = arg;
        this.isLeft = isLeft;
    }

    @Override
    public int hashCode() {
        return this.client.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BlockByBPopEvent)) {
            return false;
        }
        return this.client.equals(((BlockByBPopEvent) obj).getClient());
    }
}
