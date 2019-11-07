package com.sdw.soft.cocoim.cli;

import com.google.common.base.Splitter;
import com.sdw.soft.cocoim.protocol.ChatRequestPacket;
import com.sdw.soft.cocoim.protocol.LoginRequestPacket;
import com.sdw.soft.cocoim.protocol.Packet;
import com.sdw.soft.cocoim.utils.LoginHelper;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

/**
 * @author shangyd
 * @version 1.0.0
 * @date 2019-11-03 15:49
 * @description
 **/
public class ClientEndpoint implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(ClientEndpoint.class);

    private Channel channel;

    public ClientEndpoint(Channel channel) {
        this.channel = channel;
    }

    public enum MsgCommand {
        LOGIN("login"),CHAT("chat");
        private String cmd;

        MsgCommand(String cmd) {
            this.cmd = cmd;
        }

    }

    @Override
    public void run() {

        Scanner sc = new Scanner(System.in, "UTF-8");
        while (!Thread.interrupted()) {

//            if (LoginHelper.isLogin(channel)) {
                String line = sc.nextLine();
                logger.info("enter:{}", line);
            List<String> list = Splitter.on("::").omitEmptyStrings().splitToList(line);
            if (list.size() == 2) {
                switch (list.get(0)) {
                    case "login":
                        LoginRequestPacket login = new LoginRequestPacket();
                        login.setUserId(1l);
                        login.setUserName("Admin");
                        login.setSerialization((byte)1);
                        login.setVersion((byte) 1);
                        channel.writeAndFlush(login);

                        break;
                    case "chat":
                        ChatRequestPacket chat = new ChatRequestPacket();
                        chat.setContent(list.get(1));
                        channel.writeAndFlush(chat);
                        break;
                }
            }


//            }
        }
    }
}
