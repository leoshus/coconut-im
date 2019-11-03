package com.sdw.soft.cocoim.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author shangyd
 * @version 1.0.0
 * @date 2019-11-03 00:35
 * @description
 **/
public abstract class BaseMessageHandler<T> extends SimpleChannelInboundHandler<T> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, T msg) throws Exception {
        handleMessage(ctx, msg);
    }

    protected abstract void handleMessage(ChannelHandlerContext ctx, T msg);
}
