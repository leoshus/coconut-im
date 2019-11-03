package com.sdw.soft.cocoim.handler;

import com.sdw.soft.cocoim.codec.BinaryCodec;
import com.sdw.soft.cocoim.protocol.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

/**
 * @author shangyd
 * @version 1.0.0
 * @date 2019-11-03 14:32
 * @description
 **/
public class PacketCodecHandler extends MessageToMessageCodec<ByteBuf, Packet> {


    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, List<Object> out) throws Exception {

        ByteBuf buf = ctx.alloc().ioBuffer();
        BinaryCodec.getInstance().encode(buf, msg);
        out.add(buf);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {

        Packet packet = BinaryCodec.getInstance().decode(msg);
        out.add(packet);
    }
}
