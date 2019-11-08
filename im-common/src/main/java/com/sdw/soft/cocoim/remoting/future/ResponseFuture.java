package com.sdw.soft.cocoim.remoting.future;

import com.sdw.soft.cocoim.protocol.Packet;
import io.netty.channel.Channel;
import lombok.Data;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author: shangyd
 * @create: 2019-11-08 14:39:23
 **/
@Data
public class ResponseFuture {

    private CountDownLatch latch = new CountDownLatch(1);

    private final int opaque;
    private final Channel channel;
    private final long timeoutMillis;

    private volatile Packet result;
    private volatile Throwable throwable;
    private volatile boolean sendRequestOK;

    public ResponseFuture(int opaque, Channel channel, long timeoutMillis) {
        this.opaque = opaque;
        this.channel = channel;
        this.timeoutMillis = timeoutMillis;
    }

    public Packet waitResponse(long milliseconds) throws InterruptedException {
        latch.await(milliseconds, TimeUnit.MILLISECONDS);
        return this.result;
    }

    public void setResult(Packet packet) {
        this.result = packet;
        latch.countDown();
    }
}
