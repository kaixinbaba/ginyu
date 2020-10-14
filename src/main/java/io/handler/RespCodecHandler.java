package io.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import protocol.Resp2;
import protocol.Serializers;

import java.util.List;

/**
 * @author: junjiexun
 * @date: 2020/10/11 8:43 下午
 * @description:
 */
@ChannelHandler.Sharable
public class RespCodecHandler extends MessageToMessageCodec<ByteBuf, Resp2> {

    public static final RespCodecHandler INSTANCE = new RespCodecHandler();

    private RespCodecHandler() {
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Resp2 msg, List<Object> out) throws Exception {
        out.add(Serializers.encode(msg));
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        out.add(Serializers.decode(msg));
    }
}
