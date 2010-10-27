/* $Id$ */
package org.openttd;

import org.openttd.network.NetworkLanguage;
import org.openttd.pool.Poolable;

/**
 * OpenTTD Clients are represented in this class.
 * @author Nathanael Rebsch
 */
public class Client extends Poolable<Long>
{
    public String name;
    public int company_id;
    public NetworkLanguage language;
    public String address;
    public GameDate joindate;

    public static final int INVALID_CLIENTID = 0;
    public static final int CLIENTID_SERVER  = 1;

    public Client (long client_id)
    {
        this.id = client_id;
    }

    /**
     * Check if this client is part of a certain company
     * @param company_id The identifier of the company to check against.
     * @return true if the client is in this company.
     */
    public boolean inCompany(int company_id)
    {
        return (this.company_id == company_id);
    }

    /**
     * Check if this client is part of a certain company
     * @param company The company object to check against.
     * @return true if the client is in the company.
     */
    public boolean inCompany(Company company)
    {
        return inCompany(company.id);
    }

    /**
     * Check if the client is a spectator.
     * @return true if the client is a spectator.
     */
    public boolean isSpectator()
    {
        return (this.company_id == Company.COMPANY_SPECTATOR);
    }

    /**
     * Check if a client id is valid at all.
     * @param index The client id to check.
     * @return true if the client id is valid.
     */
    public static boolean IsValid (long index)
    {
        return (index != INVALID_CLIENTID);
    }
}
