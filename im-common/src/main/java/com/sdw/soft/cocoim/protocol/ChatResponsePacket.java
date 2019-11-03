package com.sdw.soft.cocoim.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shangyd
 * @version 1.0.0
 * @date 2019-11-03 00:47
 * @description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponsePacket extends Packet {

    private String content;
    private Long fromUserId;
    private String fromUserName;

    @Override
    public byte command() {
        return Command.CHAT_RESPONSE.getCode();
    }
}
