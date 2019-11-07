package com.sdw.soft.cocoim;

/**
 * @author: shangyd
 * @create: 2019-11-07 17:56:58
 **/
public class NameServerBootStrap {


    public static void main(String[] args) {
        NettyNameServer server = new NettyNameServer(9099);
        server.init();
        server.start(null);
    }
}
