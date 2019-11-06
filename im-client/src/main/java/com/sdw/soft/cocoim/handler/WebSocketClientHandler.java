package com.sdw.soft.cocoim.handler;

import com.sdw.soft.cocoim.protocol.LoginRequestPacket;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: shangyd
 * @create: 2019-11-06 16:54:18
 **/
public class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {


    private static final Logger logger = LoggerFactory.getLogger(WebSocketClientHandler.class);

    private final WebSocketClientHandshaker shaker;

    public WebSocketClientHandler(WebSocketClientHandshaker shaker) {
        this.shaker = shaker;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        shaker.handshake(ctx.channel());

//        super.channelActive(ctx);

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

        Channel channel = ctx.channel();
        if (!shaker.isHandshakeComplete()) {
            shaker.finishHandshake(channel, (FullHttpResponse) msg);
            return;
        }
        ctx.flush();
    }
}
