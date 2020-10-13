package io;

import config.GinyuConfig;
import io.handler.ExceptionHandler;
import io.handler.RespHandler;
import io.handler.RespCodecHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: junjiexun
 * @date: 2020/10/11 8:29 下午
 * @description:
 */
public class NettyCommunicator implements Communicator {

    private final AtomicInteger clientIdCursor = new AtomicInteger(1);

    @Override
    public void start(GinyuConfig ginyuConfig) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childAttr(AttributeKey.newInstance("clientId"), clientIdCursor.getAndIncrement())
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(RespCodecHandler.INSTANCE);
                        ch.pipeline().addLast(RespHandler.INSTANCE);
                        ch.pipeline().addLast(ExceptionHandler.INSTANCE);
                    }
                })
        ;

        serverBootstrap.bind(ginyuConfig.getPort());
    }

    @Override
    public void close() {

    }
}
