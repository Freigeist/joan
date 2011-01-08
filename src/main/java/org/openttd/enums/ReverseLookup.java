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

package org.openttd.enums;

import java.util.HashMap;

/**
 * Reverse Lookup of enumerations.
 * @author Nathanael Rebsch
 */
public class ReverseLookup<T, E extends Reversible<T>>
{
    /**
     * The reverse lookup table.
     * Only used internally.
     */
    private final HashMap<T, E> map = new HashMap<T, E>();

    /**
     * Empty constructor used for Dynamic Enums.
     */
    public ReverseLookup () {}

    /**
     * Constructor. Automatically creates a reverse lookup table if E is a subclass of Enum.
     * @param clazz Class<E extends Enum> must be true for this constructor.
     */
    public ReverseLookup (final Class<E> clazz)
    {
        for (E constant : clazz.getEnumConstants()) {
            map.put(constant.getValue(), constant);
        }
    }

    /**
     * Dynamically extend the reverse lookup table.
     * @param e Instance of the enumeration to be included in the reverse lookup table.
     */
    public void add (E e)
    {
        if (!map.containsValue(e)) {
            map.put(e.getValue(), e);
        }
    }

    /**
     * Get an instance of the enumeration corresponding to the value given.
     * @param value Value to which an instance of the enumeration should be returned.
     * @return Instance of the corresponding enumeration or null if not found.
     */
    public E get (final T value)
    {
        return map.get(value);
    }
}
