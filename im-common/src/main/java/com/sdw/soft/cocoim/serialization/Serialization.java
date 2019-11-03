package com.sdw.soft.cocoim.serialization;

import com.sdw.soft.cocoim.protocol.Packet;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shangyd
 * @version 1.0.0
 * @date 2019-11-03 00:05
 * @description
 **/
public final class Serialization {

    private static List<Serializer> serializers = new ArrayList<>();

    static {
        serializers.add(new JsonSerializer());
    }

    public Serializer choose(Packet packet) {
        return new JsonSerializer();
    }

    public Serializer choose(byte code) {
        return serializers.stream().filter(v -> v.code() == code).findFirst().get();
    }

}
