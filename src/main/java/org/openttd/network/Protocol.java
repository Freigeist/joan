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

package org.openttd.network;

import org.openttd.enums.AdminUpdateType;
import org.openttd.enums.AdminUpdateFrequency;
import java.util.EnumMap;
import java.util.EnumSet;

/**
 * Handling of Protocol details
 * @author Nathanael Rebsch
 */
public class Protocol
{
    protected int version = -1;
    protected EnumMap<AdminUpdateType,EnumSet<AdminUpdateFrequency>> supportedFrequencies;

    public Protocol ()
    {
        supportedFrequencies = new EnumMap<AdminUpdateType,EnumSet<AdminUpdateFrequency>>(AdminUpdateType.class);
    }

    /**
     * Add support of an AdminUpdateType to an AdminUpdateFrequency (if the indexes are known as such)
     * @param tIndex
     * @param fIndex
     */
    protected void addSupport (int tIndex, int fIndex)
    {
        if (AdminUpdateType.isValid(tIndex) && AdminUpdateFrequency.isValid(fIndex)) {
            AdminUpdateType type = AdminUpdateType.valueOf(tIndex);
            AdminUpdateFrequency freq = AdminUpdateFrequency.valueOf(fIndex);

            if (!supportedFrequencies.containsKey(type)) {
                supportedFrequencies.put(type, EnumSet.noneOf(AdminUpdateFrequency.class));
            }

            supportedFrequencies.get(type).add(freq);
        }
    }

    /**
     * @return The version of the current Protocol as announced by the server.
     */
    public int getVersion ()
    {
        return this.version;
    }

    /**
     * Check if the server supports the given AdminUpdateFrequency for the given AdminUpdateType
     * @param type The AdminUpdateType to check.
     * @param freq The AdminUpdateFrequency to check.
     * @return
     */
    public boolean isSupported (AdminUpdateType type, AdminUpdateFrequency freq)
    {
        return supportedFrequencies.get(type).contains(freq);
    }
}
