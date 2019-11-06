package com.sdw.soft.cocoim.utils;

import com.sdw.soft.cocoim.connection.Connection;
import io.netty.util.AttributeKey;

/**
 * @author shangyd
 * @version 1.0.0
 * @date 2019-11-03 15:53
 * @description
 **/
public interface Constant {

    AttributeKey<Connection> CONNECTION = AttributeKey.newInstance("connection");
    AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("login");
}
