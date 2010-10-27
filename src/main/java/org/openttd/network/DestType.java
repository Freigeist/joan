/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openttd.network;

/**
 *
 * @author nathanael
 */
public enum DestType
{
    DESTTYPE_BROADCAST,
    DESTTYPE_TEAM,
    DESTTYPE_CLIENT;

    public static DestType get(int i)
    {
        try {
            return DestType.values()[1 >> i];
        } catch (ArrayIndexOutOfBoundsException e) {
            return DESTTYPE_BROADCAST;
        }
    }
}
