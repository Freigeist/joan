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
public enum NetworkErrorCode implements Reverseable<Integer>
{

    NETWORK_ERROR_GENERAL            (0), // Try to use this one like never

    /* Signals from clients */
    NETWORK_ERROR_DESYNC             (1),
    NETWORK_ERROR_SAVEGAME_FAILED    (2),
    NETWORK_ERROR_CONNECTION_LOST    (3),
    NETWORK_ERROR_ILLEGAL_PACKET     (4),
    NETWORK_ERROR_NEWGRF_MISMATCH    (5),
    
    /* Signals from servers */
    NETWORK_ERROR_NOT_AUTHORIZED     (6),
    NETWORK_ERROR_NOT_EXPECTED       (7),
    NETWORK_ERROR_WRONG_REVISION     (8),
    NETWORK_ERROR_NAME_IN_USE        (9),
    NETWORK_ERROR_WRONG_PASSWORD     (10),
    NETWORK_ERROR_COMPANY_MISMATCH   (11), // Happens in CLIENT_COMMAND
    NETWORK_ERROR_KICKED             (12),
    NETWORK_ERROR_CHEATER            (13),
    NETWORK_ERROR_FULL               (14);

    private Integer value;
    private static final ReverseLookup<Integer, NetworkErrorCode> lookup = new ReverseLookup<Integer, NetworkErrorCode>(NetworkErrorCode.class);

    NetworkErrorCode (int i)
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

    public static NetworkErrorCode valueOf (int i)
    {
        return lookup.get(i);
    }
}
