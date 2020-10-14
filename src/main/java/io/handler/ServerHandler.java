package io.handler;

import common.Attributes;
import core.Client;
import core.ClientFactory;
import core.Server;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author: junjiexun
 * @date: 2020/10/14 3:29 下午
 * @description:
 */
@ChannelHandler.Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private ServerHandler() {
    }

    public static final ServerHandler INSTANCE = new ServerHandler();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 为每一个channel创建一个client
        Client client = ClientFactory.createClient();
        Attributes.setClient(client, ctx);
        Server.INSTANCE.addClient(client);
        ctx.fireChannelActive();
    }
}
