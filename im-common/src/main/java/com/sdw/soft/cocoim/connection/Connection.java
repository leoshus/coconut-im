package com.sdw.soft.cocoim.connection;

import com.sdw.soft.cocoim.protocol.Packet;
import com.sdw.soft.cocoim.session.SessionContext;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

/**
 * @author: shangyd
 * @create: 2019-11-05 17:10:11
 **/
public interface Connection {

    byte NEW = 0;
    byte CONNECTED = 1;
    byte DISCONNECTION = 2;
    byte LOGIN = 3;

    void init(Channel channel, boolean security);

    SessionContext context();

    Channel channel();

    ChannelFuture send(Packet packet);

    ChannelFuture send(Packet packet, ChannelFutureListener listener);

    ChannelFuture close();


    boolean isConnected();

    boolean isLogin();

    boolean isReadTimeout();

    boolean isWriteTimeout();

    void updateLogin(boolean logined);

    void updateLastReadTime();

    void updateLastWriteTime();
}
