package com.sdw.soft.cocoim.remoting.dispatcher;

import com.sdw.soft.cocoim.connection.Connection;
import com.sdw.soft.cocoim.remoting.command.RemotingCommand;

/**
 * @author: shangyd
 * @create: 2019-11-13 17:56:56
 **/
public interface MessageReceiver {

    public void onReceive(Connection connection, RemotingCommand command);
}
