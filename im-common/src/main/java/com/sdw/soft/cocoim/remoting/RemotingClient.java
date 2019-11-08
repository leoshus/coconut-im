package com.sdw.soft.cocoim.remoting;

import com.sdw.soft.cocoim.protocol.Packet;

/**
 * @author: shangyd
 * @create: 2019-11-07 19:58:03
 **/
public interface RemotingClient extends RemotingService {

    public Packet invokeSync(Packet req, String address, long timeoutMillis);


}
