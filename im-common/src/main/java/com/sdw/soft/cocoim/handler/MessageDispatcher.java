package com.sdw.soft.cocoim.handler;

import com.sdw.soft.cocoim.connection.Connection;
import com.sdw.soft.cocoim.protocol.Command;
import com.sdw.soft.cocoim.protocol.Packet;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author: shangyd
 * @create: 2019-11-05 17:10:52
 **/
public class MessageDispatcher implements PacketRecevier {

    private static final Map<Byte, MessageHandler> handlers = new HashMap<>();

    public void register(Command command, MessageHandler handler) {
        handlers.put(command.cmd, handler);
    }

    public void register(Command command, Supplier<MessageHandler> supplier) {
        handlers.put(command.cmd, supplier.get());
    }
    @Override
    public void onReceive(Packet packet, Connection connection) {

        MessageHandler messageHandler = handlers.get(packet.command());
        if (messageHandler != null) {
            messageHandler.handleMessage(connection, packet);
        }
    }
}
