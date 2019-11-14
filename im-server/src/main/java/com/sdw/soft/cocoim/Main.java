package com.sdw.soft.cocoim;

import com.sdw.soft.cocoim.remoting.NettyRemotingClient;
import com.sdw.soft.cocoim.remoting.RemotingClient;
import com.sdw.soft.cocoim.remoting.command.RemotingCommand;
import com.sdw.soft.cocoim.remoting.command.RemotingCommandType;
import com.sdw.soft.cocoim.server.CocoImServer;
import com.sdw.soft.cocoim.server.WebsocketServer;
import com.sdw.soft.cocoim.service.Listener;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: shangyd
 * @create: 2019-11-06 15:59:14
 **/
public class Main {

    public static void main(String[] args) {

        WebsocketServer wsServer = new WebsocketServer(8090);
        wsServer.init();
        wsServer.start(new Listener() {
            @Override
            public void onSuccess(Object... args) {
                RemotingClient client = new NettyRemotingClient();
                client.init();
                client.start();
                Map<String, Object> map = new HashMap<>();
                map.put("type", "WebSocketServer");
                map.put("host", "127.0.0.1");
                map.put("port", 8090);
                RemotingCommand command = new RemotingCommand(RemotingCommandType.BROKER_REGISTER, map);
                command = client.invokeSync(command, "127.0.0.1:10123", 3000);
                System.out.println(command.getType());

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });

        CocoImServer tcpServer = new CocoImServer(8080);
        tcpServer.init();
        tcpServer.start(new Listener() {
            @Override
            public void onSuccess(Object... args) {
                RemotingClient client = new NettyRemotingClient();
                client.init();
                client.start();
                Map<String, Object> map = new HashMap<>();
                map.put("type", "TCPServer");
                map.put("host", "127.0.0.1");
                map.put("port", 8080);
                RemotingCommand command = new RemotingCommand(RemotingCommandType.BROKER_REGISTER, map);
                command = client.invokeSync(command, "127.0.0.1:10123", 3000);
                System.out.println(command.getType());
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });


        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            wsServer.stop(new Listener() {
                @Override
                public void onSuccess(Object... args) {

                }

                @Override
                public void onFailure(Throwable throwable) {

                }
            });
            tcpServer.stop(new Listener() {
                @Override
                public void onSuccess(Object... args) {

                }

                @Override
                public void onFailure(Throwable throwable) {

                }
            });
        }));
    }
}
