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
public enum AdminCompanyRemoveReason implements Reversible<Integer>
{
    ADMIN_CRR_MANUAL     (0),
    ADMIN_CRR_AUTOCLEAN  (1),
    ADMIN_CRR_BANKRUPT   (2);

    private Integer value;
    private static final ReverseLookup<Integer, AdminCompanyRemoveReason> lookup;

    static {
        lookup = new ReverseLookup<Integer, AdminCompanyRemoveReason>(AdminCompanyRemoveReason.class);
    }

    AdminCompanyRemoveReason (int i)
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

    public String toReadableString ()
    {
        return this.toString().substring(10).toLowerCase();
    }

    public static AdminCompanyRemoveReason valueOf (int i)
    {
        return lookup.get(i);
    }
}
