/*
 *  Copyright (C) 2011 Nathanael Rebsch
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package org.openttd.enums;

/**
 *
 * @author Nathanael Rebsch
 */
public enum NetworkAction implements Reversible<Integer>
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
    private static final ReverseLookup<Integer, NetworkAction> lookup;

    static {
        lookup = new ReverseLookup<Integer, NetworkAction>(NetworkAction.class);
    }

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
