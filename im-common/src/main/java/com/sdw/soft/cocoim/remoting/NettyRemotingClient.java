package com.sdw.soft.cocoim.remoting;

import com.sdw.soft.cocoim.connection.Connection;
import com.sdw.soft.cocoim.connection.ConnectionManager;
import com.sdw.soft.cocoim.connection.NettyConnection;
import com.sdw.soft.cocoim.connection.NettyConnectionManager;
import com.sdw.soft.cocoim.exception.ImServiceException;
import com.sdw.soft.cocoim.handler.MessageDispatcher;
import com.sdw.soft.cocoim.handler.PacketCodec;
import com.sdw.soft.cocoim.handler.RegisterServiceHandler;
import com.sdw.soft.cocoim.protocol.Command;
import com.sdw.soft.cocoim.protocol.Packet;
import com.sdw.soft.cocoim.remoting.future.ResponseFuture;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author: shangyd
 * @create: 2019-11-07 19:47:40
 **/
public class NettyRemotingClient implements RemotingClient{

    private final static ConcurrentMap<String, Channel> channelCache = new ConcurrentHashMap<>();
    private final static ConcurrentMap<Integer, ResponseFuture> requestTable = new ConcurrentHashMap<>();

    private ClientHandler handler;
    private MessageDispatcher dispatcher = new MessageDispatcher();
    private ConnectionManager connectionManager = new NettyConnectionManager();
    private EventLoopGroup worker;
    private Bootstrap bootstrap;

    @Override
    public void init() {
        this.worker = new NioEventLoopGroup(1);
        this.bootstrap = new Bootstrap();
        this.handler = new ClientHandler();
        dispatcher.register(Command.REGISTER_SERVICE_REQUEST,new RegisterServiceHandler());
    }

    @Override
    public void start() {
        bootstrap = new Bootstrap();
        bootstrap.group(worker)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new PacketCodec())
                                .addLast(handler);
                    }
                });
    }

    @Override
    public void shutdown() {
        if (worker != null) {
            worker.shutdownGracefully();
        }
    }

    @Override
    public Packet invokeSync(Packet req, String address, long timeoutMillis) {
        int opaque = req.getOpaque();
        try {
            Channel channel = this.getAndCreateChannel(address);
            final ResponseFuture future = new ResponseFuture(opaque, channel, timeoutMillis);
            channel.writeAndFlush(req).addListener(f -> {
                if (f.isSuccess()) {
                    future.setSendRequestOK(true);
                    return;
                } else {
                    future.setSendRequestOK(false);
                }
                requestTable.remove(future.getOpaque());
                future.setThrowable(f.cause());
                future.setResult(null);
            });

            Packet packet = future.waitResponse(timeoutMillis);
            if (null == packet) {
                if (future.isSendRequestOK()) {

                } else {
                    throw new ImServiceException("invoke fail");
                }
            }
            return packet;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            requestTable.remove(opaque);
        }


        return null;
    }

    private Channel getAndCreateChannel(String address) {
        Channel channel = channelCache.get(address);
        if (channel == null) {
            String[] split = address.split(":");
            ChannelFuture f = bootstrap.connect(new InetSocketAddress(split[0], Integer.valueOf(split[1])));
            channel = f.syncUninterruptibly().channel();
            channelCache.putIfAbsent(address, channel);
        }
        return channel;
    }


    @ChannelHandler.Sharable
    private class ClientHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {

            Connection connection = new NettyConnection();
            connection.init(ctx.channel(), false);
            connectionManager.add(connection);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            dispatcher.onReceive((Packet) msg, connectionManager.get(ctx.channel()));
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            connectionManager.removeAndClose(ctx.channel());
        }
    }
}

