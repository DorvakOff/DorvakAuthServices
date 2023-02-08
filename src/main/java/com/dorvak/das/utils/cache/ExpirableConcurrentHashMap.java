package com.dorvak.das.utils.cache;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class ExpirableConcurrentHashMap<K, V> extends ConcurrentHashMap<K, V> implements Cleanable {

    private final Map<K, Long> timeMap = new ConcurrentHashMap<>();
    private final long expiryInMillis;
    private final Function<V, Boolean> precondition;

    public ExpirableConcurrentHashMap(long time, TimeUnit unit) {
        this(time, unit, (o) -> true);
    }

    public ExpirableConcurrentHashMap(long time, TimeUnit unit, Function<V, Boolean> precondition) {
        if (unit == TimeUnit.MILLISECONDS) {
            this.expiryInMillis = time;
        } else if (unit == TimeUnit.SECONDS) {
            this.expiryInMillis = time * 1000;
        } else if (unit == TimeUnit.MINUTES) {
            this.expiryInMillis = time * 1000 * 60;
        } else if (unit == TimeUnit.HOURS) {
            this.expiryInMillis = time * 1000 * 60 * 60;
        } else {
            throw new IllegalArgumentException(unit.toString() + " is not accepted in this case.");
        }
        this.precondition = precondition;
        CacheUtils.registerCleanable(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V put(K key, V value) {
        Date date = new Date();
        timeMap.put(key, date.getTime());
        return super.put(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (K key : m.keySet()) {
            put(key, m.get(key));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V putIfAbsent(K key, V value) {
        if (!containsKey(key)) {
            return put(key, value);
        } else {
            return get(key);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V get(Object key) {
        V value = super.get(key);
        Date date = new Date();
        timeMap.put((K) key, date.getTime());
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V getOrDefault(Object key, V defaultValue) {
        V value = super.getOrDefault(key, defaultValue);
        Date date = new Date();
        timeMap.put((K) key, date.getTime());
        return value;
    }

    /**
     * Cleans the map according to the expirations timestamps.
     */
    public void clean() {
        long currentTime = new Date().getTime();
        for (K key : timeMap.keySet()) {
            if (precondition.apply(super.get(key)) && currentTime > (timeMap.get(key) + expiryInMillis)) {
                remove(key);
                timeMap.remove(key);
            }
        }
    }
}