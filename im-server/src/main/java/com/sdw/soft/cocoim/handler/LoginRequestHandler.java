package com.sdw.soft.cocoim.handler;

import com.sdw.soft.cocoim.protocol.LoginRequestPacket;
import com.sdw.soft.cocoim.protocol.LoginResponsePacket;
import com.sdw.soft.cocoim.utils.Constant;
import com.sdw.soft.cocoim.utils.SessionHelper;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author shangyd
 * @version 1.0.0
 * @date 2019-11-03 15:17
 * @description
 **/
public class LoginRequestHandler extends BaseMessageHandler<LoginRequestPacket> {

    @Override
    protected void handleMessage(ChannelHandlerContext ctx, LoginRequestPacket msg) {
        LoginResponsePacket packet = new LoginResponsePacket();
        packet.setCode(200);
        packet.setMsg("login success");

        SessionHelper.addSession(msg.getUserId(), ctx.channel());
        ctx.channel().attr(Constant.LOGIN).set(true);
        ctx.writeAndFlush(packet);
    }
}
