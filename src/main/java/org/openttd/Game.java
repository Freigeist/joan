/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openttd;

import org.openttd.network.NetworkClient;

/**
 *
 * @author nathanael
 */
public class Game {
    public String name;
    public String version;
    public int protocol_version;
    public boolean dedicated;

    public Map map;
    public GameDate date;

    public Game ()
    {
        map = new Map();
    }

    public GameDate getDate ()
    {
        return date;
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
        return protocol_version;
    }

    public String getRevision ()
    {
        return version;
    }

    
}
