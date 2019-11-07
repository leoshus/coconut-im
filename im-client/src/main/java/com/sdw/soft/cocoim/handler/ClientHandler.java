package com.sdw.soft.cocoim.handler;

import com.sdw.soft.cocoim.connection.ConnectionManager;
import com.sdw.soft.cocoim.protocol.Packet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shangyd
 * @version 1.0.0
 * @date 2019-11-03 15:08
 * @description
 **/
public class ClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);


    private MessageDispatcher dispatcher;
    private ConnectionManager connectionManager;

    public ClientHandler(MessageDispatcher dispatcher, ConnectionManager connectionManager) {
        this.dispatcher = dispatcher;
        this.connectionManager = connectionManager;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        logger.info("read...............");
        dispatcher.onReceive((Packet) msg, connectionManager.get(ctx.channel()));
    }
}
