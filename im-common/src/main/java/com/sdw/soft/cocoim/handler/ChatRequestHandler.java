package com.sdw.soft.cocoim.handler;

import com.sdw.soft.cocoim.connection.Connection;
import com.sdw.soft.cocoim.protocol.ChatRequestPacket;
import com.sdw.soft.cocoim.protocol.ChatResponsePacket;
import com.sdw.soft.cocoim.session.Session;
import com.sdw.soft.cocoim.utils.SessionHelper;

/**
 * @author shangyd
 * @version 1.0.0
 * @date 2019-11-03 00:34
 * @description
 **/
public class ChatRequestHandler implements MessageHandler<ChatRequestPacket> {

    @Override
    public void handleMessage(Connection connection, ChatRequestPacket packet) {
        Session session = SessionHelper.getSession(connection.channel());

        ChatResponsePacket response = new ChatResponsePacket();
        response.setContent(packet.getContent());
        response.setFromUserId(session.getUserId());
        response.setFromUserName(session.getUsername());

        SessionHelper.getAllSessions().entrySet().stream()
                .forEach(v -> v.getValue().writeAndFlush(response));
    }
}
