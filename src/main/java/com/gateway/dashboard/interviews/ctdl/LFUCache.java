package com.gateway.dashboard.interviews.ctdl;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * A Least Frequently Used (LFU) cache implementation.
 *
 * <p>This cache evicts the entry that has been accessed the least number of times.
 * If there is a tie in frequency, the least recently used (LRU) entry is evicted.
 * All operations (`get` and `put`) are performed in O(1) average time complexity.
 *
 * @param <K> the type of keys maintained by this cache
 * @param <V> the type of mapped values
 */
public class LFUCache<K, V> {

    // A private record to hold the value and its access frequency.
    private static class CacheNode<V> {
        V value;
        int frequency;

        CacheNode(V value, int frequency) {
            this.value = value;
            this.frequency = frequency;
        }
    }

    private final int capacity;
    private int minFrequency;
    private final Map<K, CacheNode<V>> cache;
    private final Map<Integer, LinkedHashSet<K>> frequencyMap;

    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.minFrequency = 0;
        this.cache = new HashMap<>();
        this.frequencyMap = new HashMap<>();
    }

    public V get(K key) {
        var node = cache.get(key);
        if (node == null) {
            return null;
        }
        updateNodeFrequency(key, node);
        return node.value;
    }

    public void put(K key, V value) {
        if (capacity <= 0) { return; }
        if (cache.containsKey(key)) {
            var node = cache.get(key);
            node.value = value;
            updateNodeFrequency(key, node);
        } else {
            if (cache.size() >= capacity) { evict(); }
            var newNode = new CacheNode<>(value, 1);
            cache.put(key, newNode);
            minFrequency = 1;
            frequencyMap.computeIfAbsent(1, k -> new LinkedHashSet<>()).add(key);
        }
    }

    private void evict() {
        var minFreqKeys = frequencyMap.get(minFrequency);
        // The iterator of LinkedHashSet returns elements in insertion order (LRU)
        var keyToEvict = minFreqKeys.iterator().next();
        minFreqKeys.remove(keyToEvict);
        cache.remove(keyToEvict);
    }

    private void updateNodeFrequency(K key, CacheNode<V> node) {
        int oldFrequency = node.frequency;
        var keysWithOldFreq = frequencyMap.get(oldFrequency);
        keysWithOldFreq.remove(key);

        if (keysWithOldFreq.isEmpty() && oldFrequency == this.minFrequency) {
            minFrequency++;
        }

        node.frequency++;
        frequencyMap.computeIfAbsent(node.frequency, k -> new LinkedHashSet<>()).add(key);
    }

    public int size() {
        return cache.size();
    }
}