package com.sdw.soft.cocoim.server;

import com.sdw.soft.cocoim.handler.*;
import com.sdw.soft.cocoim.service.Listener;
import com.sdw.soft.cocoim.utils.SimpleThreadFactory;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.sdw.soft.cocoim.protocol.Command.CHAT_REQUEST;
import static com.sdw.soft.cocoim.protocol.Command.HEARTBEAT;

/**
 * @author: shangyd
 * @create: 2019-11-06 15:58:48
 **/
public class CocoImServer extends NettyTCPServer {

    private static final Logger logger = LoggerFactory.getLogger(CocoImServer.class);

    private ImServerOutAPI imServerOutAPI;
    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor(new SimpleThreadFactory("CocoImServerThread"));
    public CocoImServer(int port) {
        super(port);
    }

    @Override
    public void init() {
        super.init();
        dispatcher.register(HEARTBEAT, () -> new HeartBeatHandler());
        dispatcher.register(CHAT_REQUEST, () -> new ChatRequestHandler(connectionManager));
        this.cocoImHandler = new CocoImHandler(connectionManager, dispatcher, false);
        this.imServerOutAPI = new ImServerOutAPI();
    }

    @Override
    public void start(Listener listener) {
        super.start(listener);
        executorService.scheduleAtFixedRate(() -> {
            logger.info("CocoImServer register nameserver request.");
                    imServerOutAPI.registerNameServer("TCPServer", "127.0.0.1", this.port, 3000);
                },
                1, 5, TimeUnit.SECONDS);
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
