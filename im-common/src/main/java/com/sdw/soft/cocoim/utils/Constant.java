package com.sdw.soft.cocoim.utils;

import com.sdw.soft.cocoim.session.Session;
import io.netty.util.AttributeKey;

/**
 * @author shangyd
 * @version 1.0.0
 * @date 2019-11-03 15:53
 * @description
 **/
public interface Constant {

    AttributeKey<Session> SESSION = AttributeKey.newInstance("session");
    AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("login");
}
