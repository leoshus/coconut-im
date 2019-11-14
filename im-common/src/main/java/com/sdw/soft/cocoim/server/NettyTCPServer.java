package com.sdw.soft.cocoim.server;

import com.sdw.soft.cocoim.config.NettyConfig;
import com.sdw.soft.cocoim.connection.ConnectionManager;
import com.sdw.soft.cocoim.connection.NettyConnectionManager;
import com.sdw.soft.cocoim.exception.ErrorCode;
import com.sdw.soft.cocoim.exception.ImServiceException;
import com.sdw.soft.cocoim.handler.MessageDispatcher;
import com.sdw.soft.cocoim.service.Listener;
import com.sdw.soft.cocoim.service.Server;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Hello world!
 *
 */
public abstract class NettyTCPServer implements Server {

    private static final Logger logger = LoggerFactory.getLogger(NettyTCPServer.class);

    protected final AtomicReference<State> serverState = new AtomicReference<>(State.created);

    protected ConnectionManager connectionManager;
    protected MessageDispatcher dispatcher = new MessageDispatcher();
    protected ChannelHandler cocoImHandler;
    protected NettyConfig nettyConfig;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private boolean security;
    private Class channelClazz;

    protected String host;
    protected int port;

    public NettyTCPServer(int port) {
        this.port = port;
        this.host = null;
    }

    public NettyTCPServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void init() {
        if (!serverState.compareAndSet(State.created, State.initialized)) {
            throw new ImServiceException(ErrorCode.STARTED_SERVER_INITIAL_FAIL);
        }
        this.connectionManager = new NettyConnectionManager();

        this.nettyConfig = new NettyConfig();
        if (nettyConfig.isUseEpoll()) {
            bossGroup = new EpollEventLoopGroup(getBossNum(), getBossThreadFactory());
            workerGroup = new EpollEventLoopGroup(getWorkerNum(), getWorkerThreadFactory());
            channelClazz = EpollServerSocketChannel.class;
        } else {
            bossGroup = new NioEventLoopGroup(getBossNum(), getBossThreadFactory());
            workerGroup = new NioEventLoopGroup(getWorkerNum(), getWorkerThreadFactory());
            channelClazz = NioServerSocketChannel.class;
        }
        this.security = nettyConfig.isSecurity();
    }

    @Override
    public void start(Listener listener) {
        if (!serverState.compareAndSet(State.initialized, State.starting)) {
            throw new RuntimeException();
        }
        createServer(listener, security);
    }

    private void createServer(Listener listener, boolean security) {

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(channelClazz)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            initPipleline(ch);
                        }
                    });
            initOptions(b);
            InetSocketAddress address = Strings.isBlank(host) ? new InetSocketAddress(port) : new InetSocketAddress(host, port);
            b.bind(address).addListener(future -> {
                if (future.isSuccess()) {
                    serverState.set(State.started);
                    logger.info("NettyTCPServer start at {}", port);
                    if (listener != null) listener.onSuccess(port);
                } else {
                    logger.info("NettyTCPServer start failure at {},cause {}", port, future.cause());
                    if (listener != null) listener.onFailure(future.cause());
                }
            });
        } catch (Exception e) {
            logger.error("NettyTCPServer start failure at {},cause {}", port, e);
            if (listener != null) listener.onFailure(e);
        }
    }

    @Override
    public void stop(Listener listener) {

        if (!serverState.compareAndSet(State.started, State.shutdown)) {
            if (listener != null) {
                ImServiceException throwable = new ImServiceException("NettyTCPServer has already shutdown.");
                listener.onFailure(throwable);
            } else {
                logger.info("NettyTCPServer has already shutdown.");
            }
        } else {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            listener.onSuccess();
        }
    }

    protected void initOptions(ServerBootstrap bootstrap) {
        bootstrap.option(ChannelOption.SO_BACKLOG, nettyConfig.getTcpBackLog())
                .option(ChannelOption.SO_REUSEADDR, nettyConfig.isTcpReuseAddr())
                .childOption(ChannelOption.TCP_NODELAY, nettyConfig.isTcpNoDelay())
                .childOption(ChannelOption.SO_KEEPALIVE, nettyConfig.isTcpKeepAlive())
                .childOption(ChannelOption.SO_SNDBUF, nettyConfig.getTcpSndBuf())
                .childOption(ChannelOption.SO_RCVBUF, nettyConfig.getTcpRcvBuf());

    }

    protected abstract void initPipleline(SocketChannel ch);

    protected int getBossNum() {
        return nettyConfig.getBossThreadNum();
    }

    protected int getWorkerNum() {
        return nettyConfig.getWorkerThreadNum();
    }

    protected ThreadFactory getBossThreadFactory() {
        return new DefaultThreadFactory("CocoIm-boss");
    }
    protected ThreadFactory getWorkerThreadFactory() {
        return new DefaultThreadFactory("CocoIm-worker");
    }
}
