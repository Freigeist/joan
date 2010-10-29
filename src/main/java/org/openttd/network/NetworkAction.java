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
public enum NetworkAction implements Reverseable<Integer>
{

    NETWORK_ACTION_JOIN               (0),
    NETWORK_ACTION_LEAVE              (1),
    NETWORK_ACTION_SERVER_MESSAGE     (2),
    NETWORK_ACTION_CHAT               (3),
    NETWORK_ACTION_CHAT_COMPANY       (4),
    NETWORK_ACTION_CHAT_CLIENT        (5),
    NETWORK_ACTION_GIVE_MONEY         (6),
    NETWORK_ACTION_NAME_CHANGE        (7),
    NETWORK_ACTION_COMPANY_SPECTATOR  (8),
    NETWORK_ACTION_COMPANY_JOIN       (9),
    NETWORK_ACTION_COMPANY_NEW        (10);

    private Integer value;
    private static final ReverseLookup<Integer, NetworkAction> lookup = new ReverseLookup<Integer, NetworkAction>(NetworkAction.class);

    NetworkAction (int i)
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

    public static NetworkAction valueOf (int i)
    {
        return lookup.get(i);
    }
}
