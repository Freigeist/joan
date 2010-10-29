/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openttd.enums;

/**
 *
 * @author Nathanael Rebsch
 */
public enum AdminUpdateFrequency implements Reversible<Integer>
{
    ADMIN_FREQUENCY_POLL      (0x01),
    ADMIN_FREQUENCY_DAILY     (0x02),
    ADMIN_FREQUENCY_WEEKLY    (0x04),
    ADMIN_FREQUENCY_MONTHLY   (0x08),
    ADMIN_FREQUENCY_QUARTERLY (0x10),
    ADMIN_FREQUENCY_ANUALLY   (0x20),
    ADMIN_FREQUENCY_AUTOMATIC (0x40);

    private Integer value;
    private static final ReverseLookup<Integer, AdminUpdateFrequency> lookup = new ReverseLookup<Integer, AdminUpdateFrequency>(AdminUpdateFrequency.class);

    AdminUpdateFrequency (int i)
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

    public static AdminUpdateFrequency valueOf (int i)
    {
        return lookup.get(i);
    }
}
