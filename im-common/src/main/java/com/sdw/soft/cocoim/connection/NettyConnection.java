package com.sdw.soft.cocoim.connection;

import com.sdw.soft.cocoim.protocol.Packet;
import com.sdw.soft.cocoim.session.SessionContext;
import static com.sdw.soft.cocoim.utils.SystemHelper.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: shangyd
 * @create: 2019-11-05 19:48:05
 **/
public final class NettyConnection implements Connection ,ChannelFutureListener{

    private static final Logger logger = LoggerFactory.getLogger(NettyConnection.class);

    private Channel channel;
    private SessionContext ctx;
    private volatile byte status = NEW;
    private boolean security;
    private long lastReadTime;
    private long lastWriteTime;
    private boolean logined = false;


    @Override
    public void init(Channel channel, boolean security) {
        this.status = CONNECTED;
        this.channel = channel;
        this.ctx = new SessionContext();
        this.lastReadTime = now();
        if (security) {
            //TODO 加密连接
        }
    }

    @Override
    public SessionContext context() {
        return ctx;
    }

    @Override
    public Channel channel() {
        return channel;
    }

    @Override
    public ChannelFuture send(Packet packet) {
        return send(packet, null);
    }

    @Override
    public ChannelFuture send(Packet packet, ChannelFutureListener listener) {
        if (channel.isActive()) {
            ChannelFuture channelFuture = channel.writeAndFlush(packet);
            if (listener != null) {
                channelFuture.addListener(listener);
            }
            if (channel.isWritable()) {
                return channelFuture;
            }
            if (!channelFuture.channel().eventLoop().inEventLoop()) {
                channelFuture.awaitUninterruptibly(100);
            }
            return channelFuture;
        } else {

        }
        return this.close();
    }

    @Override
    public ChannelFuture close() {
        if (status == DISCONNECTION) return null;
        status = DISCONNECTION;
        return this.channel.close();
    }

    @Override
    public boolean isConnected() {
        return status == CONNECTED;
    }

    @Override
    public boolean isLogin() {
        return logined;
    }

    @Override
    public boolean isReadTimeout() {
        return diffNow(this.lastReadTime) > ctx.heartbeat;
    }

    @Override
    public boolean isWriteTimeout() {
        return diffNow(this.lastWriteTime) > ctx.heartbeat;
    }

    @Override
    public void updateLogin(boolean logined) {
        this.logined = logined;
    }

    @Override
    public void updateLastReadTime() {
        this.lastReadTime = now();
    }

    @Override
    public void updateLastWriteTime() {
        this.lastWriteTime = now();
    }

    @Override
    public void operationComplete(ChannelFuture future) throws Exception {
        if (future.isSuccess()) {
            this.lastWriteTime = now();
        } else {
            logger.error("NettyConnection Send msg occur error cause by:{}", future.cause());
        }
    }
}
