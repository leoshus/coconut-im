package com.sdw.soft.cocoim.handler;

import com.sdw.soft.cocoim.connection.Connection;
import com.sdw.soft.cocoim.connection.ConnectionManager;
import com.sdw.soft.cocoim.connection.NettyConnection;
import com.sdw.soft.cocoim.protocol.LoginRequestPacket;
import com.sdw.soft.cocoim.protocol.LoginResponsePacket;
import com.sdw.soft.cocoim.utils.Constant;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author shangyd
 * @version 1.0.0
 * @date 2019-11-03 15:17
 * @description
 **/
public class LoginRequestHandler extends BaseMessageHandler<LoginRequestPacket> {

    private ConnectionManager connectionManager;

    public LoginRequestHandler(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    protected void handleMessage(ChannelHandlerContext ctx, LoginRequestPacket msg) {

        //TODO 认证登录逻辑
        LoginResponsePacket response = new LoginResponsePacket();
        response.setCode(200);
        response.setMsg("login success");


        Connection connection = new NettyConnection();
        connection.init(ctx.channel(), false);
        connectionManager.add(connection);
        ctx.channel().attr(Constant.CONNECTION).set(connection);
        connection.context()
                .setUserId(msg.getUserId())
                .setUserName(msg.getUserName());
        connection.channel().attr(Constant.LOGIN).set(true);
        connection.channel().writeAndFlush(response);
    }
}
