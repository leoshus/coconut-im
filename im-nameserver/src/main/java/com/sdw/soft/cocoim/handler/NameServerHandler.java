package com.sdw.soft.cocoim.handler;

import com.sdw.soft.cocoim.connection.Connection;
import com.sdw.soft.cocoim.connection.ConnectionManager;
import com.sdw.soft.cocoim.connection.NettyConnection;
import com.sdw.soft.cocoim.protocol.Packet;
import com.sdw.soft.cocoim.remoting.command.RemotingCommand;
import com.sdw.soft.cocoim.remoting.dispatcher.RemotingMessageDispatcher;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: shangyd
 * @create: 2019-11-07 18:20:03
 **/
@ChannelHandler.Sharable
public class NameServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(NameServerHandler.class);

    private ConnectionManager connectionManager;
    private RemotingMessageDispatcher dispatcher;

    public NameServerHandler(ConnectionManager connectionManager, RemotingMessageDispatcher dispatcher) {
        this.connectionManager = connectionManager;
        this.dispatcher = dispatcher;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Connection connection = new NettyConnection();
        connection.init(ctx.channel(), false);
        connectionManager.add(connection);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        dispatcher.onReceive(connectionManager.get(ctx.channel()), (RemotingCommand) msg);
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        connectionManager.removeAndClose(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
