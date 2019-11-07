package com.sdw.soft.cocoim;

import static org.junit.Assert.assertTrue;

import com.sdw.soft.cocoim.protocol.packet.RegisterServicePacket;
import com.sdw.soft.cocoim.remoting.NettyRemotingClient;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.locks.LockSupport;

/**
 * Unit test for simple App.
 */
public class AppTest {

    private NettyRemotingClient client;
    @Before
    public void setup() {

        client = new NettyRemotingClient();
        client.init();
        client.start();
    }
    /**
     * Rigorous Test :-)
     */
    @Test
    public void testRemotingClient() {

        RegisterServicePacket req = new RegisterServicePacket();
        req.setClusterName("testCluster");
        req.setHost("127.0.0.1");
        req.setPort(8080);

        client.invokeSync(req, "127.0.0.1:10123");

        LockSupport.park();
    }
}
