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

import org.openttd.network.NetworkClient;

/**
 *
 * @author nathanael
 */
public class Game
{
    public String name;
    public String versionGame;
    public int versionProtocol;
    public boolean dedicated;

    public Map map;

    public Game ()
    {
        map = new Map();
    }

    public GameDate getDate ()
    {
        return map.dateCurrent;
    }

    public boolean isDedicated ()
    {
        return dedicated;
    }

    public Map getMap ()
    {
        return map;
    }

    public String getName ()
    {
        return name;
    }

    public int getProtocolVersion ()
    {
        return versionProtocol;
    }

    public String getRevision ()
    {
        return versionGame;
    }

    
}
