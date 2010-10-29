/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openttd;

import org.openttd.enums.Landscape;

/**
 *
 * @author nathanael
 */
public class Map {
    public String    name;
    public Landscape landscape;
    public GameDate      start_date;
    public GameDate      current_date;
    
    public long seed;
    public int  height;
    public int  width;
}
