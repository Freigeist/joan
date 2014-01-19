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
public enum PauseMode implements Reversible<Integer>
{
    PM_UNPAUSED              (0),  ///< A normal unpaused game
    PM_PAUSED_NORMAL         (1),  ///< A game normally paused
    PM_PAUSED_SAVELOAD       (2),  ///< A game paused for saving/loading
    PM_PAUSED_JOIN           (4),  ///< A game paused for 'pause_on_join'
    PM_PAUSED_ERROR          (8),  ///< A game paused because a (critical) error
    PM_PAUSED_ACTIVE_CLIENTS (16), ///< A game paused for 'min_active_clients'
    PM_PAUSED_GAME_SCRIPT    (32), ///< A game paused by a game script

    /** Pause mode bits when paused for network reasons. */
    PMB_PAUSED_NETWORK  (PM_PAUSED_ACTIVE_CLIENTS.value | PM_PAUSED_JOIN.value);

    private Integer value;
    private static final ReverseLookup<Integer, PauseMode> lookup;

    static {
        lookup = new ReverseLookup<Integer, PauseMode>(PauseMode.class);
    }

    PauseMode (int i)
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

    public static PauseMode valueOf (int i)
    {
        return lookup.get(i);
    }
}
