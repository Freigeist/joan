/*
 *  Copyright (C) 2011 Nathanael Rebsch
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

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
