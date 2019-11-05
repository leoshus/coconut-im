package com.sdw.soft.cocoim;

import com.sdw.soft.cocoim.connection.ConnectionManager;
import com.sdw.soft.cocoim.connection.NettyConnectionManager;
import com.sdw.soft.cocoim.exception.ErrorCode;
import com.sdw.soft.cocoim.exception.ImServiceException;
import com.sdw.soft.cocoim.handler.*;
import com.sdw.soft.cocoim.service.Listener;
import com.sdw.soft.cocoim.service.Server;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.sdw.soft.cocoim.protocol.Command.*;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

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

    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup childGroup;

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
        dispatcher.register(HEARTBEAT, () -> new HeartBeatHandler());
        dispatcher.register(CHAT_REQUEST, () -> new ChatRequestHandler());
        this.cocoImHandler = new CocoImHandler(connectionManager, dispatcher, false);
    }

    @Override
    public void start(Listener listener) {
        if (!serverState.compareAndSet(State.initialized, State.starting)) {
            throw new RuntimeException();
        }
        createServer(listener);
    }

    private void createServer(Listener listener) {
        bossGroup = new NioEventLoopGroup(getBossNum(), getBossThreadFactory());
        childGroup = new NioEventLoopGroup(getWorkerNum(), getWorkerThreadFactory());
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
                            initPipleline(ch);
                        }
                    });
            InetSocketAddress address = Strings.isBlank(host) ? new InetSocketAddress(port) : new InetSocketAddress(host, port);
            b.bind(address).addListener(future -> {
                if (future.isSuccess()) {
                    serverState.set(State.started);
                    logger.info("CocoImServer start at {}", port);
                    if (listener != null) listener.onSuccess(port);
                } else {
                    logger.info("CocoImServer start failure at {},cause {}", port, future.cause());
                    if (listener != null) listener.onFailure(future.cause());
                }
            });
        } catch (Exception e) {
            logger.error("CocoImServer start failure at {},cause {}", port, e);
            if (listener != null) listener.onFailure(e);
        } finally {
            bossGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }

    @Override
    public void stop(Listener listener) {

        if (!serverState.compareAndSet(State.started, State.shutdown)) {
            if (listener != null) listener.onFailure(new ImServiceException("CocoImServer has already shutdown."));
            logger.info("CocoImServer has already shutdown.");
        } else {

        }
    }

    protected void initPipleline(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new PacketCodec())
                .addLast(new LoginRequestHandler(connectionManager))
                .addLast(new AuthHandler())
                .addLast(cocoImHandler);
    }

    protected int getBossNum() {
        return 1;
    }

    protected int getWorkerNum() {
        return Runtime.getRuntime().availableProcessors();
    }

    protected ThreadFactory getBossThreadFactory() {
        return new DefaultThreadFactory("CocoIm-boss");
    }
    protected ThreadFactory getWorkerThreadFactory() {
        return new DefaultThreadFactory("CocoIm-worker");
    }
}
