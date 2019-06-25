package com.example.utils;

import org.apache.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceHelper {

    public static ExecutorService getExecutorService(int nThreads, Logger logger) {
        return Executors.newFixedThreadPool(nThreads, runnable -> {
            Thread thread = new Thread(runnable);
            if(logger.isDebugEnabled()) {
                logger.debug("Created new executor " + (thread.isDaemon() ? "daemon " : "")
                        + "thread with thread name: " + thread.getName());
            }
            return thread;
        });
    }
}
