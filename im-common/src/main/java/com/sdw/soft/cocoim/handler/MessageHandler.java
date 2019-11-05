package com.sdw.soft.cocoim.handler;

import com.sdw.soft.cocoim.connection.Connection;
import com.sdw.soft.cocoim.protocol.Packet;

/**
 * @author: shangyd
 * @create: 2019-11-05 17:13:31
 **/
public interface MessageHandler<T extends Packet> {

    void handleMessage(Connection connection, T packet);

}
