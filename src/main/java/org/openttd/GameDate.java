/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openttd;

/**
 *
 * @author nathanael
 */
public class GameDate {

    public int  year  = 0;
    public int  month = 0;
    public int  day   = 0;
    public long date;

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

        if (!isLeapYear(year) && rem >= 30+28) rem++;

        day = rem;
        setMonthDay();
    }

    public boolean isLeapYear(double year)
    {
        return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
    }

    public void setMonthDay()
    {
        if (day > 31) {
            month++;
            day -= 31;
        }

        if (day > 29) {
            month++;
            day -= 29;
        }

        if (day > 31) {
            month++;
            day -= 31;
        }

        if (day > 30) {
            month++;
            day -= 30;
        }

        if (day > 31) {
            month++;
            day -= 31;
        }

        if (day > 30) {
            month++;
            day -= 30;
        }

        if (day > 31) {
            month++;
            day -= 31;
        }

        if (day > 31) {
            month++;
            day -= 31;
        }

        if (day > 30) {
            month++;
            day -= 30;
        }

        if (day > 31) {
            month++;
            day -= 31;
        }

        if (day > 30) {
            month++;
            day -= 30;
        }

        if (day > 31) {
            month++;
            day -= 31;
        }
        
        month++;
    }

    public int getYear()
    {
        return year;
    }

    public int getMonth()
    {
        return month;
    }

    public int getDay()
    {
        return day;
    }

    public String toString()
    {
        return year+"-"+month+"-"+day;
    }
}
