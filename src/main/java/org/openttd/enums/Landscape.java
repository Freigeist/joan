/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openttd.enums;

/**
 *
 * @author Nathanael Rebsch
 */
public enum Landscape implements Reversible<Integer>
{
    LANDSCAPE_TEMPERATE  (0),
    LANDSCAPE_ARCTIC     (1),
    LANDSCAPE_TROPIC     (2),
    LANDSCAPE_TOYLAND    (3),
    NUM_LANDSCAPE        (4);

    private Integer value;
    private static final ReverseLookup<Integer, Landscape> lookup = new ReverseLookup<Integer, Landscape>(Landscape.class);

    Landscape (int i)
    {
        value = i;
    }

    public static boolean isValid (int i)
    {
        return valueOf(i) != null;
    }

    @Override
    public Integer getValue ()
    {
        return value;
    }

    public static Landscape valueOf (int i)
    {
        return lookup.get(i);
    }
}
