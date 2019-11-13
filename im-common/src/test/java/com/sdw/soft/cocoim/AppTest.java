package com.sdw.soft.cocoim;

import static org.junit.Assert.assertTrue;

import com.sdw.soft.cocoim.protocol.packet.RegisterServicePacket;
import com.sdw.soft.cocoim.remoting.NettyRemotingClient;
import com.sdw.soft.cocoim.remoting.command.RemotingCommand;
import com.sdw.soft.cocoim.remoting.command.RemotingCommandType;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
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

        Map<String, Object> properties = new HashMap<>();
        properties.put("host", "127.0.0.1");
        properties.put("port", 8080);
        RemotingCommand req = new RemotingCommand(RemotingCommandType.BROKER_REGISTER, properties);
        client.invokeSync(req, "127.0.0.1:10123", 3000);

        LockSupport.park();
    }
}
