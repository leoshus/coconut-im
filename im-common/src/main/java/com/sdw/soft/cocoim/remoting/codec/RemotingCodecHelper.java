package com.sdw.soft.cocoim.remoting.codec;

import com.sdw.soft.cocoim.remoting.command.RemotingCommand;
import com.sdw.soft.cocoim.remoting.exception.RemotingNameServerException;
import com.sdw.soft.cocoim.serialization.JsonSerializer;
import com.sdw.soft.cocoim.serialization.Serializer;
import io.netty.buffer.ByteBuf;

/**
 * @author: shangyd
 * @create: 2019-11-13 17:36:00
 **/
public class RemotingCodecHelper {

    private static final int MAGICCODE = 0x88888888;
    private static Serializer serializer = new JsonSerializer();

    public static RemotingCodecHelper getInstance() {
        return new RemotingCodecHelper();
    }
    public ByteBuf encode(ByteBuf buf, RemotingCommand command) {
        buf.writeInt(MAGICCODE);
        buf.writeByte(command.getVersion());
        byte[] serialize = serializer.serialize(command);
        int len = serialize.length;
        buf.writeInt(len);
        buf.writeBytes(serialize);
        return buf;
    }

    public RemotingCommand decode(ByteBuf buf) {

        buf.skipBytes(4);
        buf.skipBytes(1);
        int len = buf.readInt();
        byte[] buffer = new byte[len];
        buf.readBytes(buffer);
        RemotingCommand command = serializer.deserialize(RemotingCommand.class, buffer);
        if (command == null) {
            throw new RemotingNameServerException("remoting codec deserialize object occur error.");
        }
        return command;
    }

}
