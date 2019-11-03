package com.sdw.soft.cocoim.service;

/**
 * @author shangyd
 * @version 1.0.0
 * @date 2019-11-03 17:41
 * @description
 **/
public interface Listener {

    void onSuccess(Object... args);

    void onFailure(Throwable throwable);
}
