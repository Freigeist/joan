/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openttd.network;

/**
 *
 * @author nathanael
 */
public enum NetworkAction
{

    NETWORK_ACTION_JOIN,
    NETWORK_ACTION_LEAVE,
    NETWORK_ACTION_SERVER_MESSAGE,
    NETWORK_ACTION_CHAT,
    NETWORK_ACTION_CHAT_COMPANY,
    NETWORK_ACTION_CHAT_CLIENT,
    NETWORK_ACTION_GIVE_MONEY,
    NETWORK_ACTION_NAME_CHANGE,
    NETWORK_ACTION_COMPANY_SPECTATOR,
    NETWORK_ACTION_COMPANY_JOIN,
    NETWORK_ACTION_COMPANY_NEW;

    static NetworkAction get (int i)
    {
        Math.max(i, NETWORK_ACTION_JOIN.ordinal());
        Math.min(i, NETWORK_ACTION_COMPANY_NEW.ordinal());

        return NetworkAction.values()[i];
    }
}
