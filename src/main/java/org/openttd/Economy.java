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

import java.math.BigInteger;

/**
 *
 * @author nathanael
 */
public class Economy
{
    public GameDate date;

    public BigInteger money;
    public BigInteger loan;
    public BigInteger income;

    public BigInteger value;
    public int cargo;
    public int performance;

    public boolean isSameQuarter (Economy economy)
    {
        if (this.date == null || economy.date == null)
            return false;
        
        return date.getYear() == economy.date.getYear() && date.getQuarter() == economy.date.getQuarter();
    }
}
