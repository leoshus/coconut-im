package com.sdw.soft.cocoim.server;

import com.sdw.soft.cocoim.handler.*;
import com.sdw.soft.cocoim.protocol.Command;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author: shangyd
 * @create: 2019-11-06 16:22:01
 **/
public class WebsocketServer extends NettyTCPServer {

    public WebsocketServer(int port) {
        super(port);
    }

    @Override
    public void init() {
        super.init();
        dispatcher.register(Command.HEARTBEAT, new HeartBeatHandler());
        dispatcher.register(Command.CHAT_REQUEST, new ChatRequestHandler(connectionManager));
    }

    @Override
    protected void initPipleline(SocketChannel ch) {
        ch.pipeline()
                .addLast(new LoggingHandler(LogLevel.INFO))
                .addLast("idleStateHandler", new IdleStateHandler(0, 0, 60))
                .addLast("httpCodec", new HttpServerCodec())
                .addLast("aggregator", new HttpObjectAggregator(65535))
                .addLast("compressor", new HttpContentCompressor())
                .addLast("websocketHandler", new WebSocketServerProtocolHandler("/ws"))
                .addLast("byteToWebsocketEncoder", new ByteToWebsocketEncoder())
                .addLast("websocketToByteEncoder", new WebsocketToByteDecoder())
                .addLast("packetEncoder", new PacketCodec())
                .addLast("LoginHandler", new LoginRequestHandler(connectionManager))
                .addLast("AuthHandler", new AuthHandler())
                .addLast("CocoImHandler", new CocoImHandler(connectionManager, dispatcher, false));

    }
}
