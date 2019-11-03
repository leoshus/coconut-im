package com.sdw.soft.cocoim.handler;

import com.alibaba.fastjson.JSON;
import com.sdw.soft.cocoim.protocol.ChatResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shangyd
 * @version 1.0.0
 * @date 2019-11-03 16:01
 * @description
 **/
public class ChatResponseHandler extends BaseMessageHandler<ChatResponsePacket> {

    private static final Logger logger = LoggerFactory.getLogger(ChatResponseHandler.class);

    @Override
    protected void handleMessage(ChannelHandlerContext ctx, ChatResponsePacket msg) {

        logger.info("client receive msg:{}", JSON.toJSON(msg));

    }
}
