package com.sdw.soft.cocoim;

import com.sdw.soft.cocoim.server.CocoImServer;
import com.sdw.soft.cocoim.server.WebsocketServer;

/**
 * @author: shangyd
 * @create: 2019-11-06 15:59:14
 **/
public class Main {

    public static void main(String[] args) {

        WebsocketServer server = new WebsocketServer(8090);
        server.init();
        server.start(null);

        CocoImServer tcpServer = new CocoImServer(8080);
        tcpServer.init();
        tcpServer.start(null);
    }
}
