package com.sdw.soft.cocoim.remoting.codec;

import com.sdw.soft.cocoim.remoting.command.RemotingCommand;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

/**
 * @author: shangyd
 * @create: 2019-11-13 17:33:00
 **/
public class NameServerCodec extends MessageToMessageCodec<ByteBuf,RemotingCommand> {

    @Override
    protected void encode(ChannelHandlerContext ctx, RemotingCommand msg, List<Object> out) throws Exception {
        ByteBuf buffer = ctx.alloc().buffer();
        RemotingCodecHelper.getInstance().encode(buffer, msg);
        out.add(buffer);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        RemotingCommand command = RemotingCodecHelper.getInstance().decode(msg);
        out.add(command);
    }
}
