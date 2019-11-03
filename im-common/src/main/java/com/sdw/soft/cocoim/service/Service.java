package com.sdw.soft.cocoim.service;

/**
 * @author shangyd
 * @version 1.0.0
 * @date 2019-11-03 17:40
 * @description
 **/
public interface Service {

    void start(Listener listener);

    void stop(Listener listener);

    void init();
}
