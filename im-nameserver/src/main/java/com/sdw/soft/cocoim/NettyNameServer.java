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
import com.sdw.soft.cocoim.service.Listener;
import com.sdw.soft.cocoim.utils.SimpleThreadFactory;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author: shangyd
 * @create: 2019-11-07 17:58:10
 **/
public class NettyNameServer extends NettyTCPServer {


    private NameServerHandler handler;
    private RemotingMessageDispatcher dispatcher = new RemotingMessageDispatcher();
    private ConnectionManager connectionManager = new NettyConnectionManager();

    private RouteManager routeManager = new RouteManager();
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor(new SimpleThreadFactory("NameServerScanThread", true));
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
    public void start(Listener listener) {
        super.start(listener);
        executorService.scheduleAtFixedRate(() -> {
            routeManager.scanLiveBroker();
        }, 1, 3, TimeUnit.SECONDS);
    }

    @Override
    protected void initPipleline(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();
        p.addLast("nameserver-codec", new NameServerCodec())
                .addLast(handler);
    }

}
