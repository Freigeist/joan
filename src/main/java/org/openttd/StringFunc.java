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
            System.out.printf("Hex: %d %x  Letter: %s\n", i, str.codePointAt(i),str.substring(i, i+1));
            if ((str.codePointAt(i) >= 0xDB80 && str.codePointAt(i) <= 0xDBFF) ||
                (str.codePointAt(i) >= 0xE000 && str.codePointAt(i) <= 0xF8FF)) {
                    System.out.println("Deleting: " + i);
                    sb.deleteCharAt(i);
            }
        }

        return sb.toString();
    }
}
