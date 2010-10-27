/* $Id$ */
package org.openttd.pool;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Generic pool handling.
 * @author Nathanael Rebsch
 */
public abstract class GenericPool<K extends Number, V extends Poolable<K>> implements Iterable<V>,Cloneable
{

    protected HashMap<K, V> pool = new HashMap<K, V>();

    public synchronized void add (V value)
    {
        add(value.id, value);
    }

    public synchronized void add (K key, V value)
    {
        this.pool.put(key, value);
    }

    public synchronized void clear ()
    {
        this.pool.clear();
    }

    public synchronized boolean exists (K index)
    {
        return this.pool.containsKey(index);
    }

    public synchronized V get (K key)
    {
        return this.pool.get(key);
    }

    @Override
    public synchronized Iterator<V> iterator ()
    {
        return this.pool.values().iterator();
    }

    public synchronized V remove (K key)
    {
        return this.pool.remove(key);
    }

    public synchronized void set (GenericPool<K, V> p)
    {
        this.pool.clear();
        this.pool.putAll(p.pool);
    }

    public synchronized int size ()
    {
        return this.pool.size();
    }
}
