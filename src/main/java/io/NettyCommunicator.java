package io;

import config.GinyuConfig;
import io.handler.DemoInHandler;
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
                        ch.pipeline().addLast(new DemoInHandler());
                    }
                })
        ;

        serverBootstrap.bind(ginyuConfig.getPort());
    }

    public void close() {

    }
}
