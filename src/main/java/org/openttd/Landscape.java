/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openttd;

/**
 *
 * @author nathanael
 */
public enum Landscape {
    LANDSCAPE_TEMPERATE,
    LANDSCAPE_ARCTIC,
    LANDSCAPE_TROPIC,
    LANDSCAPE_TOYLAND,
    NUM_LANDSCAPE;

    public static Landscape Get(int i)
    {
        try {
            return Landscape.values()[i];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public static boolean IsValid(int i)
    {
        return Get(i) != null;
    }

    public int Ord()
    {
        return this.ordinal();
    }
}
