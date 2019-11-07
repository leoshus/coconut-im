package com.sdw.soft.cocoim;

import com.sdw.soft.cocoim.cli.ClientEndpoint;
import com.sdw.soft.cocoim.connection.ConnectionManager;
import com.sdw.soft.cocoim.connection.NettyConnectionManager;
import com.sdw.soft.cocoim.handler.*;
import com.sdw.soft.cocoim.protocol.Command;
import com.sdw.soft.cocoim.service.Listener;
import com.sdw.soft.cocoim.service.Service;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * Hello world!
 *
 */
public class CocoImClient implements Service {

    private static final Logger logger = LoggerFactory.getLogger(CocoImClient.class);

    private static final String host = "127.0.0.1";
    private static final int port = 8080;
    private ClientHandler handler;
    private MessageDispatcher dispatcher = new MessageDispatcher();
    private ConnectionManager connectionManager = new NettyConnectionManager();

    @Override
    public void init() {

        this.handler = new ClientHandler(dispatcher, connectionManager);
        this.dispatcher.register(Command.CHAT_RESPONSE, new ChatResponseHandler());
    }

    public static void main(String[] args) {

        CocoImClient client = new CocoImClient();
        client.init();
        client.start(null);
    }

    @Override
    public void start(Listener listener) {

        NioEventLoopGroup worker = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(worker)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new PacketCodec())
                                .addLast(new LoginResponseHandler(connectionManager))
                                .addLast(handler);

                    }
                });
        b.connect(new InetSocketAddress(host, port)).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {

                if (future.isSuccess()) {
                    logger.info("connect server success");
                    Thread thread = new Thread(new ClientEndpoint(future.channel()));
                    thread.start();
                } else {
                    logger.info("connect server fail,cause {}", future.cause());
                    System.exit(0);
                }
            }
        });
    }

    @Override
    public void stop(Listener listener) {

    }
}
