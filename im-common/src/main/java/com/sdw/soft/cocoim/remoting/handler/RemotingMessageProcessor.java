package com.sdw.soft.cocoim.remoting.handler;

import com.sdw.soft.cocoim.connection.Connection;
import com.sdw.soft.cocoim.remoting.command.RemotingCommand;

/**
 * @author: shangyd
 * @create: 2019-11-13 17:58:59
 **/
public interface RemotingMessageProcessor {

    void handleMessage(Connection connection, RemotingCommand command);
}
