package com.sdw.soft.cocoim.remoting;

import com.sdw.soft.cocoim.remoting.command.RemotingCommand;
import com.sdw.soft.cocoim.remoting.future.ResponseFuture;

import java.util.concurrent.ConcurrentMap;

/**
 * @author: shangyd
 * @create: 2019-11-07 19:58:03
 **/
public interface RemotingClient extends RemotingService {

    ConcurrentMap<Integer, ResponseFuture> getRequestTable();
    RemotingCommand invokeSync(RemotingCommand req, String address, long timeoutMillis);


}
