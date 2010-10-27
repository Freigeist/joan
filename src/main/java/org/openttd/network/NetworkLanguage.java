/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openttd.network;

/**
 *
 * @author nathanael
 */
public enum NetworkLanguage {
    NETLANG_ANY,
    NETLANG_ENGLISH,
    NETLANG_GERMAN,
    NETLANG_FRENCH,
    NETLANG_BRAZILIAN,
    NETLANG_BULGARIAN,
    NETLANG_CHINESE,
    NETLANG_CZECH,
    NETLANG_DANISH,
    NETLANG_DUTCH,
    NETLANG_ESPERANTO,
    NETLANG_FINNISH,
    NETLANG_HUNGARIAN,
    NETLANG_ICELANDIC,
    NETLANG_ITALIAN,
    NETLANG_JAPANESE,
    NETLANG_KOREAN,
    NETLANG_LITHUANIAN,
    NETLANG_NORWEGIAN,
    NETLANG_POLISH,
    NETLANG_PORTUGUESE,
    NETLANG_ROMANIAN,
    NETLANG_RUSSIAN,
    NETLANG_SLOVAK,
    NETLANG_SLOVENIAN,
    NETLANG_SPANISH,
    NETLANG_SWEDISH,
    NETLANG_TURKISH,
    NETLANG_UKRAINIAN,
    NETLANG_AFRIKAANS,
    NETLANG_CROATIAN,
    NETLANG_CATALAN,
    NETLANG_ESTONIAN,
    NETLANG_GALICIAN,
    NETLANG_GREEK,
    NETLANG_LATVIAN,
    NETLANG_COUNT;

    public static NetworkLanguage Get(int i)
    {
        try {
            return NetworkLanguage.values()[i];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public static boolean IsValid(int i)
    {
        return Get(i) != null;
    }

    public int Ord()
    {
        return this.ordinal();
    }
}
