package com.sdw.soft.cocoim.service;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author shangyd
 * @version 1.0.0
 * @date 2019-11-03 22:20
 * @description
 **/
public abstract class BaseService implements Service {

    protected final AtomicBoolean started = new AtomicBoolean();


}
