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
    private static final ReverseLookup<Integer, AdminUpdateType> lookup;

    static {
        lookup = new ReverseLookup<Integer, AdminUpdateType>(AdminUpdateType.class);
    }

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
