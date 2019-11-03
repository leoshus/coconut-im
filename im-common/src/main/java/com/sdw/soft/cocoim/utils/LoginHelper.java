package com.sdw.soft.cocoim.utils;

import io.netty.channel.Channel;
import io.netty.util.Attribute;

/**
 * @author shangyd
 * @version 1.0.0
 * @date 2019-11-03 15:51
 * @description
 **/
public class LoginHelper {

    public static void markAsLogin(Channel channel) {
        channel.attr(Constant.LOGIN).set(true);
    }

    public static boolean isLogin(Channel channel) {
        Attribute<Boolean> login = channel.attr(Constant.LOGIN);
        return login.get() != null ? true : false;
    }

}
