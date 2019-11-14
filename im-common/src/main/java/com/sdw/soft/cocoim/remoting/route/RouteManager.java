package com.sdw.soft.cocoim.remoting.route;

import com.alibaba.fastjson.JSONObject;
import com.sdw.soft.cocoim.utils.SystemHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.sdw.soft.cocoim.utils.SystemHelper.*;

/**
 * @author: shangyd
 * @create: 2019-11-13 18:24:32
 **/
public class RouteManager {

    private static final Logger logger = LoggerFactory.getLogger(RouteManager.class);

    private static final Map<String, List<Broker>> brokerMapping = new HashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
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

    public void registerBroker(Broker broker) {
        try {
            try {

                this.lock.writeLock().lockInterruptibly();
                List<Broker> oldBrokers = brokerMapping.get(broker.getType());

                if (oldBrokers == null) {
                    oldBrokers = new ArrayList<>();
                    oldBrokers.add(broker);
                    broker.setLastUpdateTime(SystemHelper.now());
                } else {
                    boolean isNewOne = true;
                    List<Broker> brokers = brokerMapping.get(broker.getType());
                    for (Broker b : brokers) {
                        if (b.getHost().equals(broker.getHost()) && b.getPort() == broker.getPort()) {
                            isNewOne = false;
                            b.setLastUpdateTime(now());
                        }
                    }
                    if (isNewOne) {
                        brokers.add(broker);
                        broker.setLastUpdateTime(now());
                    }
                }
            }finally {
                this.lock.writeLock().unlock();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void scanLiveBroker() {
        for (String clusterName : brokerMapping.keySet()) {
            Iterator<Broker> iterator = brokerMapping.get(clusterName).iterator();
            while (iterator.hasNext()) {
                Broker b = iterator.next();
                if (now() - b.getLastUpdateTime() > 3000) {
                    iterator.remove();
                }
            }
        }
    }
}
