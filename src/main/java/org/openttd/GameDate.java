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

package org.openttd;

import java.util.Calendar;

/**
 *
 * @author nathanael
 */
public class GameDate
{
    public int  year    = 0;
    public int  month   = 0;
    public int  day     = 0;

    public GameDate (long date)
    {
        int rem;

        /* There are 97 leap years in 400 years */
        year = (int) (400 * Math.floor(date / (365 * 400 + 97)));
        rem  = (int) (date % (365 * 400 + 97));

        /* There are 24 leap years in 100 years */
        year += (int) (100 * Math.floor(rem / (365 * 100 + 24)));
        rem   = (int) (rem % (365 * 100 + 24));

        /* There is 1 leap year every 4 years */
        year += (int) (4 * Math.floor(rem / (365 * 4 + 1)));
        rem   = (int) (rem % (365 * 4 + 1));

        while (rem >= (isLeapYear(year) ? 366 : 365)) {
            rem -= isLeapYear(year) ? 366 : 365;
            year++;
        }

        day = ++rem;
        setMonthDay();
    }
    
    public boolean isLeapYear (double year)
    {
        return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
    }

    public void setMonthDay ()
    {
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, this.year);
        cal.set(Calendar.DAY_OF_YEAR, day);

        this.month = cal.get(Calendar.MONTH) + 1;
        this.day = cal.get(Calendar.DAY_OF_MONTH);
    }
    
    public int getYear ()
    {
        return year;
    }

    public int getMonth ()
    {
        return month;
    }

    public int getDay ()
    {
        return day;
    }

    public String toString ()
    {
        return year+"-"+month+"-"+day;
    }
}
