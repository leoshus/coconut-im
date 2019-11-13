package com.sdw.soft.cocoim;

import com.sdw.soft.cocoim.connection.ConnectionManager;
import com.sdw.soft.cocoim.connection.NettyConnectionManager;
import com.sdw.soft.cocoim.handler.NameServerHandler;
import com.sdw.soft.cocoim.remoting.codec.NameServerCodec;
import com.sdw.soft.cocoim.remoting.command.RemotingCommandType;
import com.sdw.soft.cocoim.remoting.dispatcher.RemotingMessageDispatcher;
import com.sdw.soft.cocoim.remoting.handler.RegisterRemotingProcessor;
import com.sdw.soft.cocoim.remoting.route.RouteManager;
import com.sdw.soft.cocoim.server.NettyTCPServer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author: shangyd
 * @create: 2019-11-07 17:58:10
 **/
public class NettyNameServer extends NettyTCPServer {


    private NameServerHandler handler;
    private RemotingMessageDispatcher dispatcher = new RemotingMessageDispatcher();
    private ConnectionManager connectionManager = new NettyConnectionManager();

    private RouteManager routeManager = new RouteManager();

    public NettyNameServer(int port) {
        super(port);
    }

    @Override
    public void init() {
        super.init();
        this.handler = new NameServerHandler(connectionManager, dispatcher);
        routeManager.init();
        dispatcher.register(RemotingCommandType.BROKER_REGISTER, new RegisterRemotingProcessor(routeManager));
    }

    @Override
    protected void initPipleline(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();
        p.addLast("nameserver-codec", new NameServerCodec())
                .addLast(handler);
    }

}
