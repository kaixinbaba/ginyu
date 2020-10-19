package ginyu.common;

import ginyu.core.Client;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

/**
 * @author: junjiexun
 * @date: 2020/10/14 3:19 下午
 * @description:
 */
public abstract class Attributes {

    public static final AttributeKey<Client> CLIENT = AttributeKey.newInstance("client");

    public static Client getClient(ChannelHandlerContext ctx) {
        return ctx.channel().attr(CLIENT).get();
    }

    public static void setClient(Client client, ChannelHandlerContext ctx) {
        ctx.channel().attr(CLIENT).set(client);
    }

}
