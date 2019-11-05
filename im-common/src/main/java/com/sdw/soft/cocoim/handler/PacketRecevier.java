package com.sdw.soft.cocoim.handler;

import com.sdw.soft.cocoim.connection.Connection;
import com.sdw.soft.cocoim.protocol.Packet;

/**
 * @author: shangyd
 * @create: 2019-11-05 17:08:48
 **/
public interface PacketRecevier {

    void onReceive(Packet packet, Connection connection);
}
