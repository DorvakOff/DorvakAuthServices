package com.dorvak.das.utils.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class ExpirableArrayList<E> extends ArrayList<E> implements Cleanable {

    private final Map<E, Long> timeMap = new ConcurrentHashMap<>();
    private final long expiryInMillis;
    private final Function<E, Boolean> precondition;

    public ExpirableArrayList(long time, TimeUnit unit) {
        this(time, unit, (o) -> true);
    }

    public ExpirableArrayList(long time, TimeUnit unit, Function<E, Boolean> precondition) {
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
    public boolean add(E e) {
        Date date = new Date();
        timeMap.put(e, date.getTime());
        return super.add(e);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E e : c) {
            add(e);
        }
        return c.size() != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(int index, E element) {
        Date date = new Date();
        timeMap.put(element, date.getTime());
        super.add(index, element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E get(int index) {
        E element = super.get(index);
        Date date = new Date();
        timeMap.put(element, date.getTime());
        return element;
    }

    /**
     * Cleans the list according to the expirations timestamps.
     */
    public void clean() {
        long currentTime = new Date().getTime();
        for (E element : timeMap.keySet()) {
            if (precondition.apply(element) && currentTime > (timeMap.get(element) + expiryInMillis)) {
                timeMap.remove(element);
                remove(element);
            }
        }
    }
}