package com.sdw.soft.cocoim.directory;

import com.sdw.soft.cocoim.srd.vo.ServiceNode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author: shangyd
 * @create: 2019-11-07 19:02:28
 **/
public class ServerDirectoryManager {

    private final ConcurrentMap<String, List<ServiceNode>> serviceDirectory = new ConcurrentHashMap<>();

    public void add(ServiceNode serviceNode) {
        List<ServiceNode> serviceNodes = serviceDirectory.get(serviceNode.getClusterName());
        if (serviceNode == null) {
            serviceNodes = new ArrayList<>();
        }
        serviceNodes.add(serviceNode);
    }


    public void remove(ServiceNode serviceNode) {
        List<ServiceNode> serviceNodes = serviceDirectory.get(serviceNode.getClusterName());
        if (serviceNodes != null) {
            serviceNodes.remove(serviceNode);
        }
    }

    public List<ServiceNode> getAllByClusterName(String clusterName) {
        return serviceDirectory.get(clusterName);
    }
}
