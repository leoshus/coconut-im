package com.sdw.soft.cocoim.cli;

import com.sdw.soft.cocoim.protocol.ChatRequestPacket;
import com.sdw.soft.cocoim.utils.LoginHelper;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    @Override
    public void run() {

        Scanner sc = new Scanner(System.in, "UTF-8");
        while (!Thread.interrupted()) {

//            if (LoginHelper.isLogin(channel)) {
                String line = sc.nextLine();
                logger.info("enter:{}", line);
                ChatRequestPacket packet = new ChatRequestPacket();
                packet.setContent(line);
                channel.writeAndFlush(packet);
//            }
        }
    }
}
