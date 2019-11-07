package com.sdw.soft.cocoim.handler;

import com.sdw.soft.cocoim.connection.Connection;
import com.sdw.soft.cocoim.protocol.packet.RegisterServicePacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: shangyd
 * @create: 2019-11-07 20:38:48
 **/
public class RegisterServiceHandler implements MessageHandler<RegisterServicePacket> {

    private static final Logger logger = LoggerFactory.getLogger(RegisterServiceHandler.class);

    @Override
    public void handleMessage(Connection connection, RegisterServicePacket packet) {
        logger.info("register service success");
    }
}
