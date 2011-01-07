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
 *
 * @author Nathanael Rebsch
 */
public enum NetworkLanguage implements Reversible<Integer>
{
    NETLANG_ANY        (0),
    NETLANG_ENGLISH    (1),
    NETLANG_GERMAN     (2),
    NETLANG_FRENCH     (3),
    NETLANG_BRAZILIAN  (4),
    NETLANG_BULGARIAN  (5),
    NETLANG_CHINESE    (6),
    NETLANG_CZECH      (7),
    NETLANG_DANISH     (8),
    NETLANG_DUTCH      (9),
    NETLANG_ESPERANTO  (10),
    NETLANG_FINNISH    (11),
    NETLANG_HUNGARIAN  (12),
    NETLANG_ICELANDIC  (13),
    NETLANG_ITALIAN    (14),
    NETLANG_JAPANESE   (15),
    NETLANG_KOREAN     (16),
    NETLANG_LITHUANIAN (17),
    NETLANG_NORWEGIAN  (18),
    NETLANG_POLISH     (19),
    NETLANG_PORTUGUESE (20),
    NETLANG_ROMANIAN   (21),
    NETLANG_RUSSIAN    (22),
    NETLANG_SLOVAK     (23),
    NETLANG_SLOVENIAN  (24),
    NETLANG_SPANISH    (25),
    NETLANG_SWEDISH    (26),
    NETLANG_TURKISH    (27),
    NETLANG_UKRAINIAN  (28),
    NETLANG_AFRIKAANS  (29),
    NETLANG_CROATIAN   (30),
    NETLANG_CATALAN    (31),
    NETLANG_ESTONIAN   (32),
    NETLANG_GALICIAN   (33),
    NETLANG_GREEK      (34),
    NETLANG_LATVIAN    (35),
    NETLANG_COUNT      (36);

    private Integer value;
    private static final ReverseLookup<Integer, NetworkLanguage> lookup;

    static {
        lookup = new ReverseLookup<Integer, NetworkLanguage>(NetworkLanguage.class);
    }

    NetworkLanguage (int i)
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

    public static NetworkLanguage valueOf (int i)
    {
        return lookup.get(i);
    }
}
