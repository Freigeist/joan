package org.openttd.enums;

import java.util.HashMap;

/**
 *
 * @author Nathanael Rebsch
 */
public class ReverseLookup<T, E extends Enum<E> & Reverseable<T>>
{
    private final HashMap<T, E> map = new HashMap<T, E>();

    public ReverseLookup (final Class<E> c)
    {
        for (E constant : c.getEnumConstants()) {
            map.put(constant.getValue(), constant);
        }
    }

    public E get (final T value)
    {
        return map.get(value);
    }
}
