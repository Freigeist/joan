/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openttd.enums;

/**
 *
 * @author Nathanael Rebsch
 */
public enum AdminUpdateType implements Reversible<Integer>
{
    ADMIN_UPDATE_DATE            (0),
    ADMIN_UPDATE_CLIENT_INFO     (1),
    ADMIN_UPDATE_COMPANY_INFO    (2),
    ADMIN_UPDATE_COMPANY_ECONOMY (3),
    ADMIN_UPDATE_COMPANY_STATS   (4),
    ADMIN_UPDATE_CHAT            (5),
    ADMIN_UPDATE_CONSOLE         (6),
    ADMIN_UPDATE_CMD_NAMES       (7),
    ADMIN_UPDATE_CMD_LOGGING     (8),
    ADMIN_UPDATE_END             (9);

    private Integer value;
    private static final ReverseLookup<Integer, AdminUpdateType> lookup = new ReverseLookup<Integer, AdminUpdateType>(AdminUpdateType.class);

    AdminUpdateType (int i)
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

    public static AdminUpdateType valueOf (int i)
    {
        return lookup.get(i);
    }
}
