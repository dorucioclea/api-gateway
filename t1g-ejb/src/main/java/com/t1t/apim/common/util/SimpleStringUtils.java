package com.t1t.apim.common.util;

/**
 * Some simple string utils. Useful in places where we want to avoid pulling in large dependencies for small
 * tasks.
 * 
 * Where possible, handles null cases gracefully by returning the original string or null instead of
 * exceptions.
 *
 */
public class SimpleStringUtils {

    /**
     * Trim string of whitespace.
     * 
     * @param string string to trim
     * @return trimmed string, or null if null was provided.
     */
    public static String trim(String string) {
        return string == null ? null : string.trim();
    }

    /**
     * Join together varargs using a join sequence.
     * <p>
     * <tt>join("-", a, b, c) => a-b-c</tt>
     * 
     * @param joinChar character to join string
     * @param args strings to join
     * @return joined string
     */
    public static String join(String joinChar, String... args) {
        String next = ""; //$NON-NLS-1$
        StringBuffer result = new StringBuffer(length(args) + (args.length - 1));

        for (String arg : args) {
            result.append(next);
            result.append(arg);
            next = joinChar;
        }
        return result.toString();
    }

    /**
     * Cumulative length of strings in varargs
     * 
     * @param args vararg strings
     * @return cumulative length of strings
     */
    public static int length(String... args) {
        if (args == null)
            return 0;

        int acc = 0;
        for (String arg : args) {
            acc += arg.length();
        }
        return acc;
    }
}
