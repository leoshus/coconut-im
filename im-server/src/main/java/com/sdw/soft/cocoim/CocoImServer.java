package com.sdw.soft.cocoim;

import com.sdw.soft.cocoim.connection.ConnectionManager;
import com.sdw.soft.cocoim.connection.NettyConnectionManager;
import com.sdw.soft.cocoim.exception.ErrorCode;
import com.sdw.soft.cocoim.exception.ImServiceException;
import com.sdw.soft.cocoim.handler.*;
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
import static com.sdw.soft.cocoim.protocol.Command.*;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Hello world!
 *
 */
public class CocoImServer implements Server {

    private static final Logger logger = LoggerFactory.getLogger(CocoImServer.class);

    protected final AtomicReference<State> serverState = new AtomicReference<>(State.created);

    private ConnectionManager connectionManager;
    private MessageDispatcher dispatcher = new MessageDispatcher();
    private CocoImHandler cocoImHandler;
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
        CocoImServer cocoImServer = new CocoImServer(8080);
        cocoImServer.init();
        cocoImServer.start(null);
    }

    @Override
    public void init() {
        if (!serverState.compareAndSet(State.created, State.initialized)) {
            throw new ImServiceException(ErrorCode.STARTED_SERVER_INITIAL_FAIL);
        }
        this.connectionManager = new NettyConnectionManager();
        dispatcher.register(LOGIN_REQUEST, () -> new LoginRequestHandler());
        dispatcher.register(CHAT_REQUEST, () -> new ChatRequestHandler());
        this.cocoImHandler = new CocoImHandler(connectionManager, dispatcher, false);
    }

    @Override
    public void start(Listener listener) {
        if (!serverState.compareAndSet(State.initialized, State.starting)) {
            throw new RuntimeException();
        }

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
                            pipeline.addLast(new PacketCodec())
                                    .addLast(cocoImHandler);

                        }
                    });
            Channel channel = b.bind(port).syncUninterruptibly().channel();
            channel.closeFuture().syncUninterruptibly();
        }finally {
            bossGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }

    @Override
    public void stop(Listener listener) {

    }
}
