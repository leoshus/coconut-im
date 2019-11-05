package com.sdw.soft.cocoim.handler;

import com.alibaba.fastjson.JSON;
import com.sdw.soft.cocoim.protocol.LoginResponsePacket;
import com.sdw.soft.cocoim.utils.Constant;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shangyd
 * @version 1.0.0
 * @date 2019-11-03 15:44
 * @description
 **/
public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {

    private static final Logger logger = LoggerFactory.getLogger(LoginResponseHandler.class);

    public static final LoginResponseHandler INSTANCE = new LoginResponseHandler();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket msg) throws Exception {

        logger.info("client has login success:{}", JSON.toJSON(msg));

        ctx.channel().attr(Constant.LOGIN).set(true);
    }
}
