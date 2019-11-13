package com.sdw.soft.cocoim.remoting.route;

import com.alibaba.fastjson.JSONObject;
import com.sdw.soft.cocoim.utils.SystemHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: shangyd
 * @create: 2019-11-13 18:24:32
 **/
public class RouteManager {

    private static final Logger logger = LoggerFactory.getLogger(RouteManager.class);

    private static final Map<String, Broker> brokerMapping = new HashMap<>();
    private static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
        private AtomicInteger idx = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "RouteThread-" + idx.getAndIncrement());
        }
    });

    public void init() {
        Thread peekThread = new Thread(() -> {
            logger.info("current broker list:{}", JSONObject.toJSON(brokerMapping.values()));
        });
        executor.scheduleAtFixedRate(peekThread, 3, 5, TimeUnit.SECONDS);
    }

    public void addBroker(Broker broker) {

        Broker old = brokerMapping.putIfAbsent(broker.getHost(), broker);
        if (old != null) {
            old.setLastUpdateTime(SystemHelper.now());
        } else {
            broker.setLastUpdateTime(SystemHelper.now());
        }
    }

}
