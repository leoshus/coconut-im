package com.sdw.soft.cocoim.handler;

import com.sdw.soft.cocoim.connection.Connection;
import com.sdw.soft.cocoim.protocol.LoginRequestPacket;
import com.sdw.soft.cocoim.protocol.LoginResponsePacket;
import com.sdw.soft.cocoim.session.Session;
import com.sdw.soft.cocoim.utils.Constant;
import com.sdw.soft.cocoim.utils.SessionHelper;

/**
 * @author shangyd
 * @version 1.0.0
 * @date 2019-11-03 15:17
 * @description
 **/
public class LoginRequestHandler implements MessageHandler<LoginRequestPacket> {

    @Override
    public void handleMessage(Connection connection, LoginRequestPacket msg) {

        //TODO 认证登录逻辑
        LoginResponsePacket response = new LoginResponsePacket();
        response.setCode(200);
        response.setMsg("login success");



        Session session = new Session();
        session.setUserId(msg.getUserId());
        session.setUsername(msg.getUserName());
        connection.channel().attr(Constant.SESSION).set(session);

        SessionHelper.addSession(msg.getUserId(), connection.channel());
        connection.channel().attr(Constant.LOGIN).set(true);
        connection.channel().writeAndFlush(response);
    }
}
