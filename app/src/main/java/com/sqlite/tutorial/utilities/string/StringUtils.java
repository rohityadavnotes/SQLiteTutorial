package com.sqlite.tutorial.utilities.string;

import com.sqlite.tutorial.utilities.ObjectUtils;

public class StringUtils {

    private StringUtils() {
        throw new UnsupportedOperationException("You can't create instance of Util class. Please use as static..");
    }

    /**
     * Determine whether the string is null or all blank characters
     * <p>
     * isSpace(null)        = true;
     * isSpace("     ")     = true;
     * isSpace("   Hello ") = false;
     * isSpace("null")      = false;
     * isSpace("")          = true;
     *
     * @param string String to be verified
     * @return {@code true}: null or all whitespace characters<br> {@code false}: not null and not all whitespace characters
     */
    public static boolean isSpace(String string) {
        if (string == null) return true;
        for (int i = 0, len = string.length(); i < len; ++i) {
            if (!Character.isWhitespace(string.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * is null or its length is 0 or it is made by space
     * <p>
     * isBlank(null)        = true;
     * isBlank("     ")     = true;
     * isBlank("   Hello ") = false;
     * isBlank("null")      = false;
     * isBlank("")          = true;
     *
     * @param string
     * @return if string is null or its size is 0 or it is made by space, return true, else return false.
     */
    public static boolean isBlank(String string) {
        return (string == null || string.trim().length() == 0);
    }

    /**
     * is null or its length is 0
     * <p>
     *
     * isEmpty(null)        = true;
     * isEmpty("")          = true;
     * isEmpty("     ")     = false;
     * isEmpty("   Hello ") = false;
     * isEmpty("null")      = false;
     *
     * @param string
     * @return if string is null or its size is 0, return true, else return false.
     */
    public static boolean isEmpty(String string) {
        return ((string == null) || ("null".equalsIgnoreCase(string) || (string.trim().length() == 0)));
    }

    /**
     * Determine whether the given string is not null and not empty
     *
     * isNotEmpty(null)        = false;
     * isNotEmpty("")          = false;
     * isNotEmpty("     ")     = true;
     * isNotEmpty("   Hello ") = true;
     * isNotEmpty("null")      = true;
     *
     * @param string The given string
     */
    public static boolean isNotEmpty(String string) {
        return !isEmpty(string);
    }

    /**
     * null Object to empty string
     * <p>
     * nullStrToEmpty(null) = "";
     * nullStrToEmpty("")   = "";
     * nullStrToEmpty("aa") = "aa";
     *
     * @param string
     * @return
     */
    public static String convertNullStringToEmpty(Object string) {
        return (string == null ? "" : (string instanceof String ? (String) string : string.toString()));
    }

    /**
     * Return {@code ""} if string equals null.
     *
     * @param string The string.
     * @return {@code ""} if string equals null
     */
    public static String convertNullStringToLengthZero(String string) {
        return string == null ? "" : string;
    }

    /**
     * get length of CharSequence
     * <p>
     * length(null)     = 0;
     * length(\"\")     = 0;
     * length(\"abc\")  = 3;
     *
     * @param string
     * @return if string is null or empty, return 0, else return {@link CharSequence#length()}.
     */
    public static int length(String string) {
        return string == null ? 0 : string.length();
    }

    /**
     * compare two string
     *
     * @param actual
     * @param expected
     * @return if both are null, return true or if both are same, return true or if both are different, return false
     * @see ObjectUtils#isEquals(Object, Object)
     */
    public static boolean isEquals(String actual, String expected) {
        return ObjectUtils.isEquals(actual, expected);
    }

    /**
     * Return whether string1 is equals to string2, ignoring case considerations..
     *
     * isEquals(null,null)          = true;
     * isEquals("",null)            = false;
     * isEquals("","")              = true;
     * isEquals("Hello","hello")    = true;
     *
     * @param string1 The first string.
     * @param string2 The second string.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isEqualsIgnoreCase(String string1, String string2) {
        return string1 == null ? string2 == null : string1.equalsIgnoreCase(string2);
    }

    /**
     * This method validates whether string is equal to define length.
     */
    public static boolean isLength(String data, int length) {
        return data != null && data.length() == length;
    }

    /**
     * Set the first letter of string Capital.
     *
     * firstLetterCapital(null)     =   null;
     * firstLetterCapital("")       =   "";
     * firstLetterCapital("2ab")    =   "2ab"
     * firstLetterCapital("a")      =   "A"
     * firstLetterCapital("ab")     =   "Ab"
     * firstLetterCapital("Abc")    =   "Abc"
     *
     * @param string The string.
     * @return the string with first letter Capital.
     */
    public static String firstLetterCapital(String string) {
        if (isEmpty(string)) {
            return string;
        }
        if (!Character.isLowerCase(string.charAt(0))) return string;
        return String.valueOf((char) (string.charAt(0) - 32)) + string.substring(1);
    }

    /**
     * Set the first letter of string Small.
     *
     * firstLetterCapital(null)     =   null;
     * firstLetterCapital("")       =   "";
     * firstLetterCapital("2ab")    =   "2ab"
     * firstLetterCapital("A")      =   "a"
     * firstLetterCapital("Ab")     =   "ab"
     * firstLetterCapital("abc")    =   "abc"
     *
     * @param string The string.
     * @return the string with first letter Small.
     */
    public static String firstLetterSmall(String string) {
        if (isEmpty(string)) {
            return string;
        }
        if (!Character.isUpperCase(string.charAt(0))) return string;
        return String.valueOf((char) (string.charAt(0) + 32)) + string.substring(1);
    }

    /**
     * Reverse the string.
     *
     * reverse("2ab")    =   "ba2"
     *
     * @param string The string.
     * @return the reverse string.
     */
    public static String reverse(String string) {
        if (string == null) return "";
        int len = string.length();
        if (len <= 1) return string;
        int mid = len >> 1;
        char[] chars = string.toCharArray();
        char c;
        for (int i = 0; i < mid; ++i) {
            c = chars[i];
            chars[i] = chars[len - i - 1];
            chars[len - i - 1] = c;
        }
        return new String(chars);
    }
}
