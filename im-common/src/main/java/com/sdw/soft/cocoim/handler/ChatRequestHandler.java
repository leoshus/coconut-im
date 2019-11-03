package com.sdw.soft.cocoim.handler;

import com.sdw.soft.cocoim.protocol.ChatRequestPacket;
import com.sdw.soft.cocoim.protocol.ChatResponsePacket;
import com.sdw.soft.cocoim.session.Session;
import com.sdw.soft.cocoim.utils.SessionHelper;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author shangyd
 * @version 1.0.0
 * @date 2019-11-03 00:34
 * @description
 **/
public class ChatRequestHandler extends BaseMessageHandler<ChatRequestPacket> {

    @Override
    protected void handleMessage(ChannelHandlerContext ctx, ChatRequestPacket msg) {

        Session session = SessionHelper.getSession(ctx.channel());

        ChatResponsePacket response = new ChatResponsePacket();
        response.setContent(msg.getContent());
        response.setFromUserId(session.getUserId());
        response.setFromUserName(session.getUsername());

        SessionHelper.getAllSessions().entrySet().stream()
                .forEach(v -> v.getValue().writeAndFlush(response));
    }
}
