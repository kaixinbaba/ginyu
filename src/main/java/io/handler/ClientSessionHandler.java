package io.handler;

import common.Attributes;
import core.Client;
import core.Server;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author: junjiexun
 * @date: 2020/10/14 3:48 下午
 * @description:
 */
@ChannelHandler.Sharable
public class ClientSessionHandler extends IdleStateHandler {

    private static final int SESSION_TIMEOUT = 30;

    private ClientSessionHandler() {
        super(0, 0, SESSION_TIMEOUT, TimeUnit.MINUTES);
    }

    public static final ClientSessionHandler INSTANCE = new ClientSessionHandler();

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) {
        System.out.println(SESSION_TIMEOUT + "分钟内未读到数据，关闭连接");
        Client client = Attributes.getClient(ctx);
        if (client != null) {
            Server.INSTANCE.removeClient(client);
        }
        ctx.channel().close();
    }
}
