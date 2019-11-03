package com.sdw.soft.cocoim.utils;

import com.sdw.soft.cocoim.session.Session;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author shangyd
 * @version 1.0.0
 * @date 2019-11-03 00:41
 * @description
 **/
public final class SessionHelper {



    private static final Map<Long, Channel> sessions = new ConcurrentHashMap<>();

    public static Session getSession(Channel channel) {
        return channel.attr(Constant.SESSION).get();
    }


    public static Map<Long, Channel> getAllSessions() {
        return sessions;
    }

    public static void addSession(Long userId, Channel channel) {
        sessions.putIfAbsent(userId, channel);
    }

    public static void removeSession(Long userId) {
        sessions.remove(userId);
    }
}
