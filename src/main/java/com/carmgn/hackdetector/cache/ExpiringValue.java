package com.carmgn.hackdetector.cache;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

class ExpiringValue<V> implements Delayed {

    private long startTime = System.currentTimeMillis();
    private final long maxLifeTimeMillis;
    private final V value;

    public ExpiringValue(V value, long maxLifeTimeMillis) {
        this.maxLifeTimeMillis = maxLifeTimeMillis;
        this.value = value;
    }

    public V getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        final ExpiringValue<V> other = (ExpiringValue<V>) obj;
        return this.value == other.value || (this.value != null && this.value.equals(other.value));

    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.value != null ? this.value.hashCode() : 0);
        return hash;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(getDelayMillis(), TimeUnit.MILLISECONDS);
    }

    private long getDelayMillis() {
        return (startTime + maxLifeTimeMillis) - System.currentTimeMillis();
    }

    public void renew() {
        startTime = System.currentTimeMillis();
    }

    public void expire() {
        startTime = Long.MIN_VALUE;
    }

    @Override
    public int compareTo(Delayed that) {
        return Long.compare(this.getDelayMillis(), ((ExpiringValue<V>) that).getDelayMillis());
    }
}
