package com.sdw.soft.cocoim.codec;

import com.sdw.soft.cocoim.protocol.*;
import com.sdw.soft.cocoim.serialization.Serialization;
import com.sdw.soft.cocoim.serialization.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.HashMap;
import java.util.Map;

/**
 * magiccode(4)+version(1)+serialization(1)+cmd(1)+length(4)+body(n)
 *
 * @author shangyd
 * @version 1.0.0
 * @date 2019-11-02 23:47
 * @description
 **/
public class BinaryPacketCodec implements Codec {

    private static final int MAGIC_CODE = 0x12345678;

    private static Serialization serialization = new Serialization();

    private static BinaryPacketCodec codec = new BinaryPacketCodec();

    private final static Map<Byte, Class<? extends Packet>> packetMaping;


    static {
        packetMaping = new HashMap<>();
        packetMaping.put(Command.HEARTBEAT.getCmd(), HeartBeatPacket.class);
        packetMaping.put(Command.CHAT_REQUEST.getCmd(), ChatRequestPacket.class);
        packetMaping.put(Command.CHAT_RESPONSE.getCmd(), ChatResponsePacket.class);
        packetMaping.put(Command.LOGIN_REQUEST.getCmd(), LoginRequestPacket.class);
        packetMaping.put(Command.LOGIN_RESPONSE.getCmd(), LoginResponsePacket.class);

    }


    public static BinaryPacketCodec getInstance() {
        return codec;
    }
    @Override
    public ByteBuf encode(ByteBufAllocator alloc, Packet packet) {

        ByteBuf byteBuf = alloc.ioBuffer();

        return encode(byteBuf, packet);
    }

    /**
     * magiccode(4)+version(1)+serialization(1)+cmd(1)+length(4)+body(n)
     * @param buf
     * @param packet
     * @return
     */
    public ByteBuf encode(ByteBuf buf, Packet packet) {

        buf.writeInt(MAGIC_CODE);
        buf.writeByte(packet.getVersion());
        Serializer serializer = serialization.choose(packet);
        buf.writeByte(serializer.code());
        buf.writeByte(packet.command());
        byte[] data = serializer.serialize(packet);
        buf.writeInt(data.length);
        buf.writeBytes(data);
        return buf;
    }

    @Override
    public Packet decode(ByteBuf buf) {

        buf.skipBytes(4);
        buf.skipBytes(1);
        byte serializeCode = buf.readByte();
        byte cmdCode = buf.readByte();
        Serializer serializer = serialization.choose(serializeCode);

        int len = buf.readInt();
        byte[] data = new byte[len];
        buf.readBytes(data);
        Packet packet = serializer.deserialize(packetMaping.get(cmdCode), data);
        return packet;
    }
}
