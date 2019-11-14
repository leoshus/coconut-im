package com.sdw.soft.cocoim.server;

import com.sdw.soft.cocoim.handler.*;
import com.sdw.soft.cocoim.protocol.Command;
import com.sdw.soft.cocoim.service.Listener;
import com.sdw.soft.cocoim.utils.SimpleThreadFactory;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author: shangyd
 * @create: 2019-11-06 16:22:01
 **/
public class WebsocketServer extends NettyTCPServer {

    private static final Logger logger = LoggerFactory.getLogger(WebsocketServer.class);

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor(new SimpleThreadFactory("WebSocketServerThread"));
    private ImServerOutAPI imServerOutAPI;
    public WebsocketServer(int port) {
        super(port);
    }

    @Override
    public void init() {
        super.init();
        this.imServerOutAPI = new ImServerOutAPI();
        dispatcher.register(Command.HEARTBEAT, new HeartBeatHandler());
        dispatcher.register(Command.CHAT_REQUEST, new ChatRequestHandler(connectionManager));
    }

    @Override
    public void start(Listener listener) {
        super.start(listener);
        executorService.scheduleAtFixedRate(() -> {
            logger.info("WebSocket Server register nameserver request.");
                    imServerOutAPI.registerNameServer("WebSocketServer", "127.0.0.1", this.port, 3000);
                },
                1, 5, TimeUnit.SECONDS);
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
