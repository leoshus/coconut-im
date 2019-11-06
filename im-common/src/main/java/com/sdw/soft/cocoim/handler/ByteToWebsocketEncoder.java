package com.sdw.soft.cocoim.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

import java.util.List;

/**
 * @author: shangyd
 * @create: 2019-11-06 16:34:05
 **/
public class ByteToWebsocketEncoder extends MessageToMessageEncoder<ByteBuf> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        BinaryWebSocketFrame frame = new BinaryWebSocketFrame();
        frame.content().writeBytes(msg);
        out.add(frame);
    }
}
