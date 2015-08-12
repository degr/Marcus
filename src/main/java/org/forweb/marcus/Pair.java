package org.forweb.marcus;

/**
 * Created by rsmirnou on 8/11/2015. 05
 */
public class Pair<K, I> {
    public K key;
    public I item;

    public Pair(K key, I item) {
        this.key = key;
        this.item = item;
    }
}
