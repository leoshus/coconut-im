package com.sdw.soft.cocoim.serialization;

import com.alibaba.fastjson.JSON;
import com.sdw.soft.cocoim.protocol.Packet;

/**
 * @author shangyd
 * @version 1.0.0
 * @date 2019-11-03 00:03
 * @description
 **/
public class JsonSerializer implements Serializer {

    private static final byte code = 1;

    @Override
    public boolean match(Packet packet) {
        return code == packet.getSerialization().byteValue();
    }

    @Override
    public byte[] serialize(Object obj) {
        return JSON.toJSONBytes(obj);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] data) {
        return JSON.parseObject(data, clazz);
    }

    @Override
    public byte code() {
        return code;
    }
}
