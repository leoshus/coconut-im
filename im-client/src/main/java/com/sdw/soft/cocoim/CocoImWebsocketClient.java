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
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.URI;

/**
 * @author: shangyd
 * @create: 2019-11-06 16:45:18
 **/
public class CocoImWebsocketClient implements Service {

    private static final Logger logger = LoggerFactory.getLogger(CocoImWebsocketClient.class);

    private ClientHandler handler;
    private MessageDispatcher dispatcher = new MessageDispatcher();
    private ConnectionManager connectionManager = new NettyConnectionManager();
    @Override
    public void init() {
        this.handler = new ClientHandler(dispatcher, connectionManager);
        dispatcher.register(Command.CHAT_RESPONSE, new ChatResponseHandler());
    }

    public static void main(String[] args) {
        CocoImWebsocketClient client = new CocoImWebsocketClient();
        client.init();
        client.start(null);
    }

    @Override
    public void start(Listener listener) {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast("http-codec", new HttpClientCodec())
                                .addLast("aggregator", new HttpObjectAggregator(65536))
                                .addLast(new ByteToWebsocketEncoder())
                                .addLast(new WebsocketToByteDecoder())
                                .addLast("packetEncoder", new PacketCodec())
                                .addLast("LoginHandler", new LoginResponseHandler(connectionManager))
                                .addLast("websocketHandler", new WebSocketClientHandler(WebSocketClientHandshakerFactory.newHandshaker(new URI("ws://localhost:8090/ws"), WebSocketVersion.V13, null, true, new DefaultHttpHeaders())))
                                .addLast("CocoImHandler", handler);
                    }
                });
        ChannelFuture channelFuture = b.connect(new InetSocketAddress(8090)).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {

                if (future.isSuccess()) {
                    logger.info("connect websocket server success.");

                    Thread scan = new Thread(new ClientEndpoint(future.channel()));
                    scan.start();
                } else {
                    logger.info("connect websocket server fail, cause {}", future.cause());
                }
            }
        });
        channelFuture.syncUninterruptibly();
    }

    @Override
    public void stop(Listener listener) {

    }
}
