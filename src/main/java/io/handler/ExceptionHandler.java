package io.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import protocol.Errors;

/**
 * @author: junjiexun
 * @date: 2020/10/11 10:24 下午
 * @description:
 */
@SuppressWarnings("all")
@ChannelHandler.Sharable
public class ExceptionHandler extends ChannelInboundHandlerAdapter {

    public static final ExceptionHandler INSTANCE = new ExceptionHandler();

    private ExceptionHandler() {
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.writeAndFlush(Errors.create(cause.getMessage()));
    }
}
