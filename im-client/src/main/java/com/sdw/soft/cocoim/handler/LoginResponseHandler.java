package com.sdw.soft.cocoim.handler;

import com.alibaba.fastjson.JSON;
import com.sdw.soft.cocoim.connection.Connection;
import com.sdw.soft.cocoim.connection.ConnectionManager;
import com.sdw.soft.cocoim.connection.NettyConnection;
import com.sdw.soft.cocoim.protocol.LoginResponsePacket;
import com.sdw.soft.cocoim.utils.Constant;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shangyd
 * @version 1.0.0
 * @date 2019-11-03 15:44
 * @description
 **/
public class LoginResponseHandler extends BaseMessageHandler<LoginResponsePacket> {

    private static final Logger logger = LoggerFactory.getLogger(LoginResponseHandler.class);

    private ConnectionManager connectionManager;

    public LoginResponseHandler(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    protected void handleMessage(ChannelHandlerContext ctx, LoginResponsePacket msg) {
        logger.info("client has login success:{}", JSON.toJSON(msg));

        ctx.channel().attr(Constant.LOGIN).set(true);
        Connection connection = new NettyConnection();
        connection.init(ctx.channel(), false);
        connectionManager.add(connection);
    }
}
