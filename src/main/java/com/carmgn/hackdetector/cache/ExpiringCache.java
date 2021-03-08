package com.carmgn.hackdetector.cache;


import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;

import javax.ejb.Singleton;

import org.springframework.stereotype.Component;

@Singleton
@Component
public class ExpiringCache<K,V> implements Map<K, V> {

    private final Map<K, DelayQueue<ExpiringValue<V>>> expiringIpMap = new ConcurrentHashMap<>();

    /**
     * The default max life time in milliseconds.
     */
    private final long maxLifeTimeMillis;

    public ExpiringCache() {
        this.maxLifeTimeMillis = Long.MAX_VALUE;

    }

    public ExpiringCache(long defaultMaxLifeTimeMillis) {
        this.maxLifeTimeMillis = defaultMaxLifeTimeMillis;
    }

    @Override
    public int size() {
        cleanup();
        return expiringIpMap.size();
    }

    @Override
    public boolean isEmpty() {
        cleanup();
        return expiringIpMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        cleanup();
        return expiringIpMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        cleanup();
        return expiringIpMap.containsValue(value);
    }

    @Override
    public V get(Object key) {
        cleanup();
        if(expiringIpMap.get(key) == null || expiringIpMap.get(key).peek() == null){
            return null;
        }
        return expiringIpMap.get(key).peek().getValue();
    }

    @Override
    public V put(K key, V value) {
        return this.put(key, value, maxLifeTimeMillis);
    }

    public V put(K key, V value, long lifeTimeMillis) {
        ExpiringValue<V> delayedValue = new ExpiringValue<V>(value, lifeTimeMillis);
        DelayQueue<ExpiringValue<V>> delayQueue = expiringIpMap.get((K) key);
        if (delayQueue == null){
            delayQueue = new DelayQueue<>();
        }
        delayQueue.put(delayedValue);
        expiringIpMap.put(key, delayQueue);
        cleanup();
        return value;
    }

    private void cleanup() {
        expiringIpMap.forEach((k,v) -> clean(v));
    }

    private void clean(DelayQueue<ExpiringValue<V>> v) {
        ExpiringValue<V> delayedValue = v.poll();
        while (delayedValue != null) {
            v.remove(delayedValue);
            delayedValue = v.poll();
        }
    }

    @Override
    public V remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {

    }

    @Override
    public void clear() {
        expiringIpMap.clear();
    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

}
