package com.dorvak.das.utils.cache;

import com.dorvak.das.utils.MultiThreading;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CacheUtils {

    public static final int REFRESH_TIME_SECONDS = 5;
    private static final List<Cleanable> cleanableList = new ArrayList<>();
    private static boolean initialized;

    public static void init() {
        if (!initialized) {
            MultiThreading.schedule(CacheUtils::cleanAll, REFRESH_TIME_SECONDS, REFRESH_TIME_SECONDS, TimeUnit.SECONDS);
            initialized = true;
        }
    }

    public static void registerCleanable(Cleanable cleanable) {
        cleanableList.add(cleanable);
    }

    public static void cleanAll() {
        if (cleanableList.size() < 150) {
            cleanableList.forEach(Cleanable::clean);
        } else {
            cleanableList.parallelStream().forEach(Cleanable::clean);
        }
    }
}