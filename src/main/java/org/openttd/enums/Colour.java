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
    DARK_BLUE   (0),
    PALE_GREEN  (1),
    PINK        (2),
    YELLOW      (3),
    RED         (4),
    LIGHT_BLUE  (5),
    GREEN       (6),
    DARK_GREEN  (7),
    BLUE        (8),
    CREAM       (9),
    MAUVE       (10),
    PURPLE      (11),
    ORANGE      (12),
    BROWN       (13),
    GREY        (14),
    WHITE       (15),
    END         (16),
    INVALID     (0xFF);

    private Integer value;
    private static final ReverseLookup<Integer, Colour> lookup;

    static {
        lookup = new ReverseLookup<Integer, Colour>(Colour.class);
    }

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
