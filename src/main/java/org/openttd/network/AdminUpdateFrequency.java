/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openttd.network;

import java.util.EnumSet;

/**
 *
 * @author nathanael
 */
public enum AdminUpdateFrequency
{
    ADMIN_FREQUENCY_POLL      (0x01),
    ADMIN_FREQUENCY_DAILY     (0x02),
    ADMIN_FREQUENCY_WEEKLY    (0x04),
    ADMIN_FREQUENCY_MONTHLY   (0x08),
    ADMIN_FREQUENCY_QUARTERLY (0x10),
    ADMIN_FREQUENCY_ANUALLY   (0x20),
    ADMIN_FREQUENCY_AUTOMATIC (0x40);

    private int value;
    private static final EnumSet<AdminUpdateFrequency> frequencies = EnumSet.allOf(AdminUpdateFrequency.class);

    AdminUpdateFrequency (int i)
    {
        this.value = i;
    }

    public static AdminUpdateFrequency get(int i)
    {
        for (AdminUpdateFrequency freq : frequencies) {
            if (freq.getValue() == i)
                return freq;
        }

        return null;
    }

    public static boolean isValid(int i)
    {
        return get(i) != null;
    }

    public int getValue()
    {
        return value;
    }
}
