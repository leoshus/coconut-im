package com.sdw.soft.cocoim.protocol;

import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * magiccode(4)+version(1)+serialization(1)+cmd(1)+length(4)+body(n)
 * magiccode(4)+version(1)+serialization(1)+cmd(1)+length(4)+cc(2)+flags(1)+sessionId(4)+lrc(1)+body(n)
 *
 * @author shangyd
 * @version 1.0.0
 * @date 2019-11-02 23:29
 * @description
 **/
@Data
public abstract class Packet {

    private static AtomicInteger requestId = new AtomicInteger(0);

    private int opaque = requestId.getAndIncrement();

    private Byte version = 1;

    private Byte serialization = 1;

    public abstract byte command();

}
