package com.sdw.soft.cocoim.remoting.handler;

import com.sdw.soft.cocoim.connection.Connection;
import com.sdw.soft.cocoim.remoting.RemotingClient;
import com.sdw.soft.cocoim.remoting.command.RemotingCommand;
import com.sdw.soft.cocoim.remoting.future.ResponseFuture;

/**
 * @author: shangyd
 * @create: 2019-11-13 20:43:12
 **/
public class ResponseRemotingProcessor implements RemotingMessageProcessor {

    private RemotingClient client;

    public ResponseRemotingProcessor(RemotingClient client) {
        this.client = client;
    }

    @Override
    public void handleMessage(Connection connection, RemotingCommand command) {
        int opaque = command.getOpaque();
        ResponseFuture future = this.client.getRequestTable().get(opaque);
        future.setResult(command);

    }
}
