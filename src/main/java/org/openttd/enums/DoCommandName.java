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

import java.util.ArrayList;
import java.util.List;


/**
 * Dynamic enumeration, to represent an enumeration as received from the server.
 * @author Nathanael Rebsch
 */
public class DoCommandName implements Reversible<Integer>
{
    protected final String name;
    protected final Integer value;
    
    private static final List<DoCommandName> enumeration;
    private static final ReverseLookup<Integer,DoCommandName> lookup;

    static {
        enumeration = new ArrayList<DoCommandName>();
        lookup = new ReverseLookup<Integer,DoCommandName>();
    }

    /**
     * Constructor, adds another DoCommandName type to the Dynamic enumeration.
     * @param name The name of this enumeration.
     * @param value The value of this enumeration.
     */
    public DoCommandName (String name, Integer value)
    {
        this.name  = name;
        this.value = value;

        enumeration.add(this);
        lookup.add(this);
    }

    /**
     * Get an instance of the DoCommandNmae with the index of i.
     * @param i Index of the desired DoCommandName.
     * @return Instance of DoCommandName or null.
     */
    public DoCommandName valueOf (int i)
    {
        return lookup.get(value);
    }

    @Override
    public Integer getValue()
    {
        return this.value;
    }

    @Override
    public String toString ()
    {
        return this.name;
    }

    public static DoCommandName[] values ()
    {
        DoCommandName[] a = new DoCommandName[enumeration.size()];
        return enumeration.toArray(a);
    }
}
