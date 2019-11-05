package com.sdw.soft.cocoim.handler;

import com.sdw.soft.cocoim.protocol.LoginRequestPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author shangyd
 * @version 1.0.0
 * @date 2019-11-03 15:08
 * @description
 **/
public class ClientHandler extends ChannelInboundHandlerAdapter {

    public static final ClientHandler INSTANCE = new ClientHandler();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LoginRequestPacket packet = new LoginRequestPacket();
        packet.setUserId(1l);
        packet.setUserName("admin");
        ctx.writeAndFlush(packet);
    }
}
