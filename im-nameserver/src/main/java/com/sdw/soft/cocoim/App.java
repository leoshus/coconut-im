package com.sdw.soft.cocoim;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) {

        NettyNameServer server = new NettyNameServer(10123);
        server.init();
        server.start(null);
    }
}
