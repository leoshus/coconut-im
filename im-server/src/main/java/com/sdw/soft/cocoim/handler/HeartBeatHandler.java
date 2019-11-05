package com.sdw.soft.cocoim.handler;

import com.alibaba.fastjson.JSON;
import com.sdw.soft.cocoim.connection.Connection;
import com.sdw.soft.cocoim.protocol.HeartBeatPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shangyd
 * @version 1.0.0
 * @date 2019-11-03 15:38
 * @description
 **/
public class HeartBeatHandler implements MessageHandler<HeartBeatPacket> {

    private static final Logger logger = LoggerFactory.getLogger(HeartBeatHandler.class);

    @Override
    public void handleMessage(Connection connection, HeartBeatPacket packet) {
        logger.info("server receive heartbeat:{}", JSON.toJSON(packet));
        connection.channel().writeAndFlush(packet);
    }
}
