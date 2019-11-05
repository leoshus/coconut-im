package com.sdw.soft.cocoim.handler;

import com.sdw.soft.cocoim.connection.Connection;
import com.sdw.soft.cocoim.protocol.Packet;

/**
 * @author: shangyd
 * @create: 2019-11-05 17:35:09
 **/
public abstract class CommonHandler<T extends Packet> implements MessageHandler {

    public abstract T decode(Connection connection, Packet packet);

    public abstract void handle(T msg);

    @Override
    public void handleMessage(Connection connection, Packet packet) {
        T msg = decode(connection, packet);
        if (msg != null) {
            handle(msg);
        }
    }
}
