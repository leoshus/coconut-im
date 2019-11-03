package com.sdw.soft.cocoim.codec;

import com.sdw.soft.cocoim.protocol.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * @author shangyd
 * @version 1.0.0
 * @date 2019-11-02 23:47
 * @description
 **/
public interface Codec {

    ByteBuf encode(ByteBufAllocator alloc, Packet packet);

    ByteBuf encode(ByteBuf buf, Packet packet);

    Packet decode(ByteBuf buf);

}
