/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openttd.network;

/**
 *
 * @author nathanael
 */
public enum NetworkErrorCode
{

    NETWORK_ERROR_GENERAL, // Try to use this one like never

    /* Signals from clients */
    NETWORK_ERROR_DESYNC,
    NETWORK_ERROR_SAVEGAME_FAILED,
    NETWORK_ERROR_CONNECTION_LOST,
    NETWORK_ERROR_ILLEGAL_PACKET,
    NETWORK_ERROR_NEWGRF_MISMATCH,
    
    /* Signals from servers */
    NETWORK_ERROR_NOT_AUTHORIZED,
    NETWORK_ERROR_NOT_EXPECTED,
    NETWORK_ERROR_WRONG_REVISION,
    NETWORK_ERROR_NAME_IN_USE,
    NETWORK_ERROR_WRONG_PASSWORD,
    NETWORK_ERROR_COMPANY_MISMATCH, // Happens in CLIENT_COMMAND
    NETWORK_ERROR_KICKED,
    NETWORK_ERROR_CHEATER,
    NETWORK_ERROR_FULL;

    public static NetworkErrorCode get (int i) {
        i = Math.min(i, NETWORK_ERROR_FULL.ordinal());
        i = Math.max(i, NETWORK_ERROR_GENERAL.ordinal());

        return NetworkErrorCode.values()[i];
    }

    public static void printErrorCode (int i)
    {
        System.out.printf("The server has sent an error: %s\n", NetworkErrorCode.get(i));
    }
}
