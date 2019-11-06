package com.sdw.soft.cocoim.server;

import com.sdw.soft.cocoim.handler.*;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

import static com.sdw.soft.cocoim.protocol.Command.CHAT_REQUEST;
import static com.sdw.soft.cocoim.protocol.Command.HEARTBEAT;

/**
 * @author: shangyd
 * @create: 2019-11-06 15:58:48
 **/
public class CocoImServer extends NettyTCPServer {


    public CocoImServer(int port) {
        super(port);
    }

    @Override
    public void init() {
        super.init();
        dispatcher.register(HEARTBEAT, () -> new HeartBeatHandler());
        dispatcher.register(CHAT_REQUEST, () -> new ChatRequestHandler(connectionManager));
        this.cocoImHandler = new CocoImHandler(connectionManager, dispatcher, false);
    }


    @Override
    protected void initPipleline(SocketChannel ch) {

        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new PacketCodec())
                .addLast(new LoginRequestHandler(connectionManager))
                .addLast(new AuthHandler())
                .addLast(cocoImHandler);
    }
}
