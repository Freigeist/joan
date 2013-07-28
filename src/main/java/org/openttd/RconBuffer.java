/*
 * Copyright (C) 2013 nathanael
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.openttd;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.openttd.enums.Colour;

/**
 *
 * @author nathanael
 */
public class RconBuffer implements Iterable<RconBuffer.Entry>
{
    public class Entry
    {
        public final Colour colour;
        public final String message;
        
        public Entry (Colour colour, String message)
        {
            this.colour = colour;
            this.message = message;
        }
    }
    
    private Set<Entry> buffer;
    private boolean eor = false;
    
    public RconBuffer ()
    {
        buffer = new HashSet<Entry>();
    }
    
    public void add (Colour colour, String message)
    {
        this.buffer.add(new Entry(colour, message));
    }
    
    public void setEOR ()
    {
        this.eor = true;
    }
    
    public boolean isEOR ()
    {
        return this.eor;
    }

    public int size()
    {
        return this.buffer.size();
    }

    @Override
    public Iterator<Entry> iterator()
    {
        return this.buffer.iterator();
    }

    public boolean isEmpty()
    {
        return this.buffer.isEmpty();
    }    
}
