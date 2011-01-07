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

/**
 * Colour enum as found in OpenTTD.
 * @author Nathanael Rebsch
 */
public enum Colour implements Reversible<Integer>
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
