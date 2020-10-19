package ginyu.cmd.list;

import ginyu.core.Client;
import io.netty.channel.ChannelHandlerContext;

public class BPopWatcher implements Comparable<BPopWatcher> {

    private final Client client;

    private final ChannelHandlerContext ctx;

    private final BPopArg arg;

    public BPopWatcher(Client client, ChannelHandlerContext ctx, BPopArg arg) {
        this.client = client;
        this.ctx = ctx;
        this.arg = arg;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BPopWatcher)) {
            return false;
        }
        return this.client.equals(((BPopWatcher) obj).client);
    }

    @Override
    public int hashCode() {
        return this.client.hashCode();
    }

    public void callback() {

    }

    @Override
    public int compareTo(BPopWatcher o) {
        return this.client.getId().compareTo(o.client.getId());
    }
}
