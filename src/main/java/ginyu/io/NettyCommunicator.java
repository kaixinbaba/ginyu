package ginyu.io;

import ginyu.config.GinyuConfig;
import ginyu.io.handler.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author: junjiexun
 * @date: 2020/10/11 8:29 下午
 * @description:
 */
public class NettyCommunicator implements Communicator {

    @Override
    public void start(GinyuConfig ginyuConfig) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new ClientSessionHandler());
                        ch.pipeline().addLast(RespCodecHandler.INSTANCE);
                        ch.pipeline().addLast(ServerHandler.INSTANCE);
                        ch.pipeline().addLast(RespHandler.INSTANCE);
                        ch.pipeline().addLast(ExceptionHandler.INSTANCE);
                    }
                });

        serverBootstrap.bind(ginyuConfig.getPort());
    }

    @Override
    public void close() {

    }
}
