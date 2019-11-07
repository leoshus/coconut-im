package com.sdw.soft.cocoim.zk;

import com.sdw.soft.cocoim.srd.vo.ServiceNode;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

/**
 * @author: shangyd
 * @create: 2019-11-07 17:21:25
 **/
public class ZkClientBootstrap {


    private static final String DISCOVERY_PATH = "/discovery/tcpServer";
    public static void main(String[] args) throws Exception {

        CuratorFramework client = null;
        ServiceDiscovery<ServiceNode> serviceDiscovery = null;
        try {
            client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
            client.start();

            JsonInstanceSerializer<ServiceNode> serializer = new JsonInstanceSerializer<>(ServiceNode.class);
            serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceNode.class).client(client).basePath(DISCOVERY_PATH).serializer(serializer).build();
            serviceDiscovery.start();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            CloseableUtils.closeQuietly(serviceDiscovery);
            CloseableUtils.closeQuietly(client);
        }


    }

}
