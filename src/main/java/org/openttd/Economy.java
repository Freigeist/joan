/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openttd;

import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nathanael
 */
public class Economy
{
    public int year;
    public int quarter;

    public BigInteger money;
    public BigInteger loan;
    public BigInteger income;

    public BigInteger value;
    public BigInteger cargo;
    public BigInteger performance;

    public boolean isSameQuarter(Economy economy)
    {
        return year == economy.year && quarter == economy.quarter;
    }
}
