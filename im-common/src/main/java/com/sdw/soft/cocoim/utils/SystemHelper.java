package com.sdw.soft.cocoim.utils;

/**
 * @author: shangyd
 * @create: 2019-11-05 20:01:57
 **/
public final class SystemHelper {

    public static long now() {
        return System.currentTimeMillis();
    }

    public static long diffNow(long time) {
        return now() - time;
    }
}
