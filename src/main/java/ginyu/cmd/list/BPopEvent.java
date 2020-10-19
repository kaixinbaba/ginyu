package ginyu.cmd.list;

import ginyu.core.Client;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;

@SuppressWarnings("all")
@Getter
public class BPopEvent implements Comparable<BPopEvent> {

    private final Client client;

    private final ChannelHandlerContext ctx;

    private final BPopArg arg;

    private final Boolean isLeft;

    private final String[] keys;

    public BPopEvent(Client client, ChannelHandlerContext ctx,
                     BPopArg arg, Boolean isLeft,

                     String... keys) {
        this.client = client;
        this.ctx = ctx;
        this.arg = arg;
        this.isLeft = isLeft;
        this.keys = keys;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BPopEvent)) {
            return false;
        }
        return this.client.equals(((BPopEvent) obj).client);
    }

    @Override
    public int hashCode() {
        return this.client.hashCode();
    }

    @Override
    public int compareTo(BPopEvent o) {
        return this.client.getId().compareTo(o.client.getId());
    }
}
