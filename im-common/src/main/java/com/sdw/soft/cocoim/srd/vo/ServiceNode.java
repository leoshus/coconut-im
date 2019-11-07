package com.sdw.soft.cocoim.srd.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: shangyd
 * @create: 2019-11-07 17:38:11
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceNode {
    private String clusterName;
    private String host;
    private int port;

}
