package com.sdw.soft.cocoim.serialization;

import com.sdw.soft.cocoim.protocol.Packet;

/**
 * @author shangyd
 * @version 1.0.0
 * @date 2019-11-03 00:00
 * @description
 **/
public interface Serializer {

    boolean match(Packet packet);

    byte[] serialize(Object obj);

    <T> T deserialize(Class<T> clazz, byte[] data);

    byte code();
}
