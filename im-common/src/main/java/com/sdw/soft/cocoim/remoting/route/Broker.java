package com.sdw.soft.cocoim.remoting.route;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: shangyd
 * @create: 2019-11-13 19:30:14
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Broker {

    private String type;
    private String host;
    private int port;
    private long lastUpdateTime;

}
