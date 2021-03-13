package com.sqlite.tutorial.utilities;

public class ObjectUtils {

    private ObjectUtils() {
        throw new UnsupportedOperationException("You can't create instance of Util class. Please use as static..");
    }

    /**
     * compare two object
     * 
     * @param actual
     * @param expected
     * @return if both are null, return true or if both are same, return true or if both are different, return false
     *
     * isEquals(null,null)          = true;
     * isEquals("",null)            = false;
     * isEquals("","")              = true;
     * isEquals("hello","hello")    = true;
     *
     */
    public static boolean isEquals(Object actual, Object expected) {
        return actual == expected || (actual == null ? expected == null : actual.equals(expected));
    }
}
