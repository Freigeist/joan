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

package org.openttd;

import org.openttd.enums.NetworkLanguage;
import org.openttd.pool.Poolable;

/**
 * OpenTTD Clients are represented in this class.
 * @author Nathanael Rebsch
 */
public class Client extends Poolable<Long>
{
    public String name;
    public int companyId;
    public NetworkLanguage language;
    public String address;
    public GameDate joindate;

    public static final int INVALID_CLIENTID = 0;
    public static final int CLIENTID_SERVER  = 1;

    public Client (long clientId)
    {
        super(clientId);
    }

    /**
     * Check if this client is part of a certain company
     * @param companyId The identifier of the company to check against.
     * @return true if the client is in this company.
     */
    public boolean inCompany (int companyId)
    {
        return (this.companyId == companyId);
    }

    /**
     * Check if this client is part of a certain company
     * @param company The company object to check against.
     * @return true if the client is in the company.
     */
    public boolean inCompany (Company company)
    {
        return inCompany(company.id);
    }

    /**
     * Check if the client is a spectator.
     * @return true if the client is a spectator.
     */
    public boolean isSpectator ()
    {
        return (this.companyId == Company.COMPANY_SPECTATOR);
    }

    /**
     * Check if a client id is valid at all.
     * @param index The client id to check.
     * @return true if the client id is valid.
     */
    public static boolean isValid (long index)
    {
        return (index != INVALID_CLIENTID);
    }
}
