package com.sdw.soft.cocoim;

import com.sdw.soft.cocoim.cli.ClientEndpoint;
import com.sdw.soft.cocoim.handler.ChatResponseHandler;
import com.sdw.soft.cocoim.handler.ClientHandler;
import com.sdw.soft.cocoim.handler.LoginResponseHandler;
import com.sdw.soft.cocoim.handler.PacketCodec;
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
public class CocoImClient {

    private static final Logger logger = LoggerFactory.getLogger(CocoImClient.class);

    private static final String host = "127.0.0.1";
    private static final int port = 8080;

    public static void main(String[] args) {
        NioEventLoopGroup worker = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(worker)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {

                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new PacketCodec())
                                .addLast(new LoginResponseHandler())
                                .addLast(new ChatResponseHandler())
                                .addLast(ClientHandler.INSTANCE);

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
}
