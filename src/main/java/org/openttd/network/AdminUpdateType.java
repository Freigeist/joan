/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openttd.network;

/**
 *
 * @author nathanael
 */
public enum AdminUpdateType
{
    ADMIN_UPDATE_DATE,
    ADMIN_UPDATE_CLIENT_INFO,
    ADMIN_UPDATE_COMPANY_INFO,
    ADMIN_UPDATE_COMPANY_ECONOMY,
    ADMIN_UPDATE_COMPANY_STATS,
    ADMIN_UPDATE_CHAT,
    ADMIN_UPDATE_CONSOLE,
    ADMIN_UPDATE_END;

    public static AdminUpdateType get(int i)
    {
        try {
            return AdminUpdateType.values()[i];
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
        return this.ordinal();
    }
}
