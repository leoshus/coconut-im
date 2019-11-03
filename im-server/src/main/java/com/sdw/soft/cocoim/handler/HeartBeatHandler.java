package com.sdw.soft.cocoim.handler;

import com.alibaba.fastjson.JSON;
import com.sdw.soft.cocoim.protocol.HeartBeatPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shangyd
 * @version 1.0.0
 * @date 2019-11-03 15:38
 * @description
 **/
public class HeartBeatHandler extends SimpleChannelInboundHandler<HeartBeatPacket> {

    private static final Logger logger = LoggerFactory.getLogger(HeartBeatHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HeartBeatPacket msg) throws Exception {
        logger.info("server receive heartbeat:{}", JSON.toJSON(msg));

        ctx.writeAndFlush(msg);
    }
}
