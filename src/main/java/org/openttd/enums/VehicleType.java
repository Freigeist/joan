/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openttd.enums;

/**
 *
 * @author Nathanael Rebsch
 */
public enum VehicleType implements Reverseable<Integer>
{
    NETWORK_VEH_TRAIN  (0),
    NETWORK_VEH_LORRY  (1),
    NETWORK_VEH_BUS    (2),
    NETWORK_VEH_PLANE  (3),
    NETWORK_VEH_SHIP   (4),
    NETWORK_VEH_END    (5);

    private Integer value;
    private static final ReverseLookup<Integer, VehicleType> lookup = new ReverseLookup<Integer, VehicleType>(VehicleType.class);

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
