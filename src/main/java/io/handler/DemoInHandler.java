package io.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author: junjiexun
 * @date: 2020/10/11 10:24 下午
 * @description:
 */
public class DemoInHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("demo in " + msg);
        ctx.writeAndFlush(msg);
    }
}
