package com.sdw.soft.cocoim;

import com.sdw.soft.cocoim.handler.ChatRequestHandler;
import com.sdw.soft.cocoim.handler.CocoImHandler;
import com.sdw.soft.cocoim.handler.LoginRequestHandler;
import com.sdw.soft.cocoim.handler.PacketCodecHandler;
import com.sdw.soft.cocoim.service.Listener;
import com.sdw.soft.cocoim.service.Server;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Hello world!
 *
 */
public class CocoImServer implements Server {

    private static final Logger logger = LoggerFactory.getLogger(CocoImServer.class);

    protected final AtomicReference<State> serverState = new AtomicReference<>(State.created);
    private String host;
    private int port;

    public CocoImServer(int port) {
        this.port = port;
        this.host = null;
    }

    public CocoImServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) {

        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup childGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());
        try {

            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, childGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_SNDBUF, 32 * 1024)
                    .childOption(ChannelOption.SO_RCVBUF, 32 * 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {

                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new PacketCodecHandler())
                                    .addLast(new ChatRequestHandler())
                                    .addLast(new LoginRequestHandler())
                                    .addLast(new CocoImHandler());

                        }
                    });
            Channel channel = b.bind(8080).syncUninterruptibly().channel();
            channel.closeFuture().syncUninterruptibly();
        }finally {
            bossGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }

    @Override
    public void init() {
        if (!serverState.compareAndSet(State.created, State.initialized)) {

        }
    }

    @Override
    public void start(Listener listener) {

        if (!serverState.compareAndSet(State.initialized, State.starting)) {
            throw new RuntimeException();
        }
    }

    @Override
    public void stop(Listener listener) {

    }
}
