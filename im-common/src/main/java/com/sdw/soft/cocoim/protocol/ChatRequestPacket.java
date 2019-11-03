package com.sdw.soft.cocoim.protocol;

import lombok.Data;

/**
 * @author shangyd
 * @version 1.0.0
 * @date 2019-11-03 00:36
 * @description
 **/
@Data
public class ChatRequestPacket extends Packet {

    private String content;
    private Long userId;
    private Long toUserId;

    @Override
    public byte command() {
        return Command.CHAT_REQUEST.getCode();
    }
}
