package com.sdw.soft.cocoim;

import com.sdw.soft.cocoim.connection.ConnectionManager;
import com.sdw.soft.cocoim.connection.NettyConnectionManager;
import com.sdw.soft.cocoim.handler.MessageDispatcher;
import com.sdw.soft.cocoim.handler.NameServerHandler;
import com.sdw.soft.cocoim.handler.PacketCodec;
import com.sdw.soft.cocoim.handler.RegisterServiceHandler;
import com.sdw.soft.cocoim.protocol.Command;
import com.sdw.soft.cocoim.server.NettyTCPServer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author: shangyd
 * @create: 2019-11-07 17:58:10
 **/
public class NettyNameServer extends NettyTCPServer {


    private NameServerHandler handler;
    private MessageDispatcher dispatcher = new MessageDispatcher();
    private ConnectionManager connectionManager = new NettyConnectionManager();

    public NettyNameServer(int port) {
        super(port);
    }

    @Override
    public void init() {
        super.init();
        this.handler = new NameServerHandler(connectionManager, dispatcher);
        dispatcher.register(Command.REGISTER_SERVICE_REQUEST, new RegisterServiceHandler());
    }

    @Override
    protected void initPipleline(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();
        p.addLast("im-codec", new PacketCodec())
                .addLast(handler);
    }

}
