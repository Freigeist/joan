/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openttd;

/**
 *
 * @author nathanael
 */
public final class StringFunc {

    public static String stripColour (String str)
    {
        StringBuilder sb = new StringBuilder(str);

        for (int i = 0; i < str.length(); i++) {
            if ((str.codePointAt(i) >= 0xDB80 && str.codePointAt(i) <= 0xDBFF) ||
                (str.codePointAt(i) >= 0xE000 && str.codePointAt(i) <= 0xF8FF)) {
                    sb.deleteCharAt(i);
            }
        }

        return sb.toString();
    }
}
