/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openttd.network;

import org.openttd.enums.ReverseLookup;
import org.openttd.enums.Reverseable;

/**
 *
 * @author nathanael
 */
public enum DestType implements Reverseable<Integer>
{
    DESTTYPE_BROADCAST (0),
    DESTTYPE_TEAM      (1),
    DESTTYPE_CLIENT    (2);

    private Integer value;
    private static final ReverseLookup<Integer, DestType> lookup = new ReverseLookup<Integer, DestType>(DestType.class);

    DestType (int i)
    {
        value = i;
    }

    public static boolean isValid (int i)
    {
        return valueOf(i) != null;
    }

    @Override
    public Integer getValue ()
    {
        return value;
    }

    public static DestType valueOf (int i)
    {
        return lookup.get(i);
    }
}
