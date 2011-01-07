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

/**
 *
 * @author Nathanael Rebsch
 */
public enum VehicleType implements Reversible<Integer>
{
    NETWORK_VEH_TRAIN  (0),
    NETWORK_VEH_LORRY  (1),
    NETWORK_VEH_BUS    (2),
    NETWORK_VEH_PLANE  (3),
    NETWORK_VEH_SHIP   (4);

    private Integer value;
    private static final ReverseLookup<Integer, VehicleType> lookup;

    static {
        lookup = new ReverseLookup<Integer, VehicleType>(VehicleType.class);
    }

    VehicleType (int i)
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

    public static VehicleType valueOf (int i)
    {
        return lookup.get(i);
    }
}
