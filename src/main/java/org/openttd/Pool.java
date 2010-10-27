/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openttd;

import org.openttd.pool.CompanyPool;
import org.openttd.pool.ClientPool;

/**
 *
 * @author nathanael
 */
public class Pool
{
    protected ClientPool  client_pool;
    protected CompanyPool company_pool;

    public Pool ()
    {
        client_pool  = new ClientPool();
        company_pool = new CompanyPool();
    }

    public ClientPool getClientPool()
    {
        return client_pool;
    }

    public CompanyPool getCompanyPool()
    {
        return company_pool;
    }
}
