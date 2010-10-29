/* $Id */
package org.openttd;

import org.openttd.enums.ReverseLookup;
import org.openttd.enums.Reverseable;

/**
 * Colour enum as found in OpenTTD.
 * @author Nathanael Rebsch
 */
public enum Colour implements Reverseable<Integer>
{
    COLOUR_DARK_BLUE   (0),
    COLOUR_PALE_GREEN  (1),
    COLOUR_PINK        (2),
    COLOUR_YELLOW      (3),
    COLOUR_RED         (4),
    COLOUR_LIGHT_BLUE  (5),
    COLOUR_GREEN       (6),
    COLOUR_DARK_GREEN  (7),
    COLOUR_BLUE        (8),
    COLOUR_CREAM       (9),
    COLOUR_MAUVE       (10),
    COLOUR_PURPLE      (11),
    COLOUR_ORANGE      (12),
    COLOUR_BROWN       (13),
    COLOUR_GREY        (14),
    COLOUR_WHITE       (15),
    COLOUR_END         (16),
    INVALID_COLOUR     (0xFF);

    private Integer value;
    private static final ReverseLookup<Integer, Colour> lookup = new ReverseLookup<Integer, Colour>(Colour.class);

    Colour (int i)
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

    public static Colour valueOf (int i)
    {
        return lookup.get(i);
    }
}
