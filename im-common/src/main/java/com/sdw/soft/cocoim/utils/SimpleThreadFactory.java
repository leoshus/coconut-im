package com.sdw.soft.cocoim.utils;

import com.google.common.base.Joiner;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: shangyd
 * @create: 2019-11-14 15:09:42
 **/
public class SimpleThreadFactory implements ThreadFactory {
    private final AtomicInteger idx = new AtomicInteger(0);
    private final String threadNamePrefix;
    private final boolean isDaemon;

    public SimpleThreadFactory(String threadNamePrefix) {
        this(threadNamePrefix, false);
    }

    public SimpleThreadFactory(String threadNamePrefix, boolean isDaemon) {
        this.threadNamePrefix = threadNamePrefix;
        this.isDaemon = isDaemon;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r, Joiner.on("-").useForNull("").join(threadNamePrefix, idx.incrementAndGet()));
        thread.setDaemon(isDaemon);
        return thread;

    }
}
