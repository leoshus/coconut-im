package com.sdw.soft.cocoim.handler;

import com.sdw.soft.cocoim.connection.Connection;
import com.sdw.soft.cocoim.connection.ConnectionManager;
import com.sdw.soft.cocoim.connection.NettyConnection;
import com.sdw.soft.cocoim.protocol.Packet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shangyd
 * @version 1.0.0
 * @date 2019-11-02 23:18
 * @description
 **/
public class CocoImHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = LoggerFactory.getLogger(CocoImHandler.class);

    private ConnectionManager connectionManager;
    private PacketRecevier recevier;
    private boolean security;//是否启动加密

    public CocoImHandler(ConnectionManager connectionManager, PacketRecevier recevier, boolean security) {
        this.connectionManager = connectionManager;
        this.recevier = recevier;
        this.security = security;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        Packet packet = (Packet) msg;
        Connection connection = connectionManager.get(ctx.channel());
        connection.updateLastReadTime();
        recevier.onReceive(packet, connection);
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("client connected conn={}", ctx.channel());
        /*Connection connection = new NettyConnection();
        connection.init(ctx.channel(), false);
        connectionManager.add(connection);*/
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        connectionManager.removeAndClose(ctx.channel());
        logger.info("client disconnected conn={}", ctx.channel());
    }
}
