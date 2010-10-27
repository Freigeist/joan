/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openttd.network;

/**
 *
 * @author nathanael
 */
public enum AdminUpdateFrequency
{
    ADMIN_FREQUENCY_NONE,
    ADMIN_FREQUENCY_DAILY,
    ADMIN_FREQUENCY_WEEKLY,
    ADMIN_FREQUENCY_MONTHLY,
    ADMIN_FREQUENCY_QUARTERLY,
    ADMIN_FREQUENCY_ANUALLY,
    ADMIN_FREQUENCY_AUTOMATIC,
    ADMIN_FREQUENCY_END;

    public static AdminUpdateFrequency get(int i)
    {
        try {
            return AdminUpdateFrequency.values()[1 >> i];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public static boolean isValid(int i)
    {
        return get(i) != null;
    }

    public int getValue()
    {
        return 1 << this.ordinal();
    }
}
