package me.wuwenbin.modules.utils.lang;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 快捷操作Map相关工具
 * created by Wuwenbin on 2017/12/19 at 10:54
 *
 * @author Wuwenbin
 */
public final class Maps<K, V> {

    /**
     * 默认Map的初始容量：16 - 必须是2的幂
     */
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;

    private TreeSet<K> keys;
    private TreeSet<V> values;


    public static Maps builder() {
        return new Maps<>();
    }

    public Maps<K, V> key(K key) {
        if (this.keys == null) {
            this.keys = new TreeSet<>();
        }
        if (this.keys.size() == 0 || (this.keys.size() == this.values.size())) {
            this.keys.add(key);
        } else {
            throw new RuntimeException("生成Map过程中出错。keys 与 values 数量不匹配，请检查 [" + key + "] 之前定义的 key 或者 val！");
        }
        return this;
    }

    public Maps<K, V> keys(K... keys) {
        Set<K> keySet = new TreeSet<>(Arrays.asList(keys));
        return keys(keySet);
    }

    public Maps<K, V> keys(Collection<K> keys) {

        if (this.keys == null) {
            this.keys = new TreeSet<>();
        }
        if (this.keys.size() == 0 || (this.keys.size() == this.values.size())) {
            this.keys.addAll(keys);
        } else {
            throw new RuntimeException("生成Map过程中出错。keys 与 values 数量不匹配，请检查定义的 key 或者 val！");
        }
        return this;
    }

    public Maps<K, V> val(V value) {
        if (this.keys == null || this.keys.isEmpty()) {
            throw new RuntimeException("生成Map过程中出错。请在赋值 values 之前先赋值 key！");
        } else {
            if (this.values == null) {
                this.values = new TreeSet<>();
            }
            if (this.keys.size() - 1 == this.values.size()) {
                this.values.add(value);
            } else {
                throw new RuntimeException("生成Map过程中出错。请在赋值 values 之前先赋值 key！");
            }
        }
        return this;
    }

    public Maps<K, V> vals(V... vals) {
        Set<V> valSet = new TreeSet<>(Arrays.asList(vals));
        return vals(valSet);
    }

    public Maps<K, V> vals(Collection<V> values) {
        if (this.keys == null || this.keys.isEmpty()) {
            throw new RuntimeException("生成Map过程中出错。请在赋值 values 之前先赋值 key！");
        } else {
            if (this.values == null) {
                this.values = new TreeSet<>();
            }
            if (this.keys.size() - 1 == this.values.size()) {
                this.values.addAll(values);
            } else {
                throw new RuntimeException("生成Map过程中出错。请在赋值 values 之前先赋值 key！");
            }
        }
        return this;
    }

    /**
     * 快速生成一个TreeMap
     *
     * @return
     */
    public TreeMap<K, V> treeMap() {
        if (this.keys == null || this.keys.isEmpty()) {
            return new TreeMap<>();
        } else {
            TreeMap<K, V> treeMap = new TreeMap<>();
            while (keys.iterator().hasNext()) {
                K key = keys.iterator().next();
                V value = values.iterator().next();
                treeMap.put(key, value);
            }
            return treeMap;
        }
    }

    /**
     * 快速生成一个HashMap
     *
     * @return
     */
    public HashMap<K, V> hashMap() {
        if (this.keys == null || this.keys.isEmpty()) {
            return new HashMap<>(DEFAULT_INITIAL_CAPACITY);
        } else {
            HashMap<K, V> hashMap = new HashMap<>(this.keys.size());
            while (keys.iterator().hasNext()) {
                K key = keys.iterator().next();
                V value = values.iterator().next();
                hashMap.put(key, value);
            }
            return hashMap;
        }
    }

    /**
     * 快速生成一个ConcurrentHashMap
     *
     * @return
     */
    public ConcurrentHashMap<K, V> concurrentHashMap() {
        if (this.keys == null || this.keys.isEmpty()) {
            return new ConcurrentHashMap<>(DEFAULT_INITIAL_CAPACITY);
        } else {
            ConcurrentHashMap<K, V> concurrentHashMap = new ConcurrentHashMap<>(this.keys.size());
            while (keys.iterator().hasNext()) {
                K key = keys.iterator().next();
                V value = values.iterator().next();
                concurrentHashMap.put(key, value);
            }
            return concurrentHashMap;
        }
    }

}
