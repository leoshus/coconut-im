package com.sdw.soft.cocoim.connection;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author: shangyd
 * @create: 2019-11-05 20:37:55
 **/
public final class NettyConnectionManager implements ConnectionManager {

    private static ConcurrentMap<ChannelId, Connection> connections = new ConcurrentHashMap<>();

    @Override
    public Connection get(Channel channel) {
        return connections.get(channel.id());
    }

    @Override
    public Connection removeAndClose(Channel channel) {
        Connection conn = connections.remove(channel.id());
        conn.close();
        return conn;
    }

    @Override
    public void add(Connection connection) {
        connections.putIfAbsent(connection.channel().id(), connection);
    }

    @Override
    public int getConnNum() {
        return connections.size();
    }

    @Override
    public void init() {

    }

    @Override
    public void destroy() {
        connections.values().forEach(Connection::close);
        connections.clear();
    }

    @Override
    public Collection<Connection> getAll() {
        return connections.values();
    }
}
