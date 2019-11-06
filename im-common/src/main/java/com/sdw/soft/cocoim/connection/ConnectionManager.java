package com.sdw.soft.cocoim.connection;

import io.netty.channel.Channel;

import java.util.Collection;

/**
 * @author: shangyd
 * @create: 2019-11-05 20:09:12
 **/
public interface ConnectionManager {

    Connection get(Channel channel);

    Connection removeAndClose(Channel channel);

    void add(Connection connection);

    int getConnNum();

    void init();

    void destroy();

    Collection<Connection> getAll();

}
