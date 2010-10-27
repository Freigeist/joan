/* $Id */
package org.openttd;

/**
 * Colour enum as found in OpenTTD.
 * @author Nathanael Rebsch
 */
public enum Colour {
    COLOUR_DARK_BLUE,
    COLOUR_PALE_GREEN,
    COLOUR_PINK,
    COLOUR_YELLOW,
    COLOUR_RED,
    COLOUR_LIGHT_BLUE,
    COLOUR_GREEN,
    COLOUR_DARK_GREEN,
    COLOUR_BLUE,
    COLOUR_CREAM,
    COLOUR_MAUVE,
    COLOUR_PURPLE,
    COLOUR_ORANGE,
    COLOUR_BROWN,
    COLOUR_GREY,
    COLOUR_WHITE,
    COLOUR_END;
    
    public static int INVALID_COLOUR = 0xFF;

    /**
     * Get the Enum to the interger.
     * @param i The index to retrieve the Enum for.
     * @return Colour
     */
    public static Colour get(int i)
    {
        try {
            return Colour.values()[i];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Check if the index is a valid enum member.
     * @param i The index to check for.
     * @return true if the index is valid.
     */
    public static boolean isValid(int i)
    {
        return get(i) != null;
    }

    /**
     * Get the numeric representation of the Enum.
     * @return int Numeric representation of the Enum.
     */
    public int getValue()
    {
        return this.ordinal();
    }
}
