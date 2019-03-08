package com.coolsen.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;


import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by YeKai on 2019/3/8
 */
public class OrderNumUtils {

    private static final ReentrantLock lock = new ReentrantLock();
    // 默认1个大小
    private static HashMap<String, AtomicInteger> cacheMap = new HashMap<>(1);

    public static String createOrderNo() {
        String timestamp;
        String inc;
        lock.lock();
        try {
            timestamp = DateTime.now().toString("yyyyMMddHHmmssSSS");
            AtomicInteger value = cacheMap.get(timestamp);
            if (value == null) {
                cacheMap.clear();
                int defaultStartValue = 0;
                cacheMap.put(timestamp, new AtomicInteger(defaultStartValue));
                inc = String.valueOf(defaultStartValue);
            } else {
                inc = String.valueOf(value.addAndGet(1));
            }
        } finally {
            lock.unlock();
        }
        return timestamp + StringUtils.leftPad(inc, 3, '0');
    }
}
