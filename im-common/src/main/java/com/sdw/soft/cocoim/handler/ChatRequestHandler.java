package com.sdw.soft.cocoim.handler;

import com.sdw.soft.cocoim.connection.Connection;
import com.sdw.soft.cocoim.connection.ConnectionManager;
import com.sdw.soft.cocoim.protocol.ChatRequestPacket;
import com.sdw.soft.cocoim.protocol.ChatResponsePacket;
import com.sdw.soft.cocoim.session.SessionContext;

/**
 * @author shangyd
 * @version 1.0.0
 * @date 2019-11-03 00:34
 * @description
 **/
public class ChatRequestHandler implements MessageHandler<ChatRequestPacket> {

    private ConnectionManager connectionManager;

    public ChatRequestHandler(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public void handleMessage(Connection connection, ChatRequestPacket packet) {
        SessionContext context = connection.context();

        ChatResponsePacket response = new ChatResponsePacket();
        response.setContent(packet.getContent());
        response.setFromUserId(context.getUserId());
        response.setFromUserName(context.getUserName());

        connectionManager.getAll().stream()
                .forEach(v -> v.channel().writeAndFlush(response));
    }
}
