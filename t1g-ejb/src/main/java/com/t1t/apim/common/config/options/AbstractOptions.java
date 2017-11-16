package com.t1t.apim.common.config.options;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * Base class for all options.
 */
public abstract class AbstractOptions {

    /**
     * Constructor. Parses options immediately.
     *
     * @param options the options
     */
    public AbstractOptions(Map<String, String> options) {
        parse(options);
    }

    protected static String getVar(Map<String, String> optionsMap, String varName) {
        if (optionsMap.get(varName) == null || optionsMap.get(varName).isEmpty()) {
            return null;
        }
        return optionsMap.get(varName);
    }

    protected static String[] split(String str, char splitter) {
        if (str == null)
            return null;

        String[] splitStr = StringUtils.split(str, splitter);

        String[] out = new String[splitStr.length];

        for (int i = 0; i < splitStr.length; i++) {
            out[i] = StringUtils.trim(splitStr[i]);
        }

        return out;
    }

    protected static boolean parseBool(Map<String, String> optionsMap, String key) {
        return parseBool(optionsMap, key, false);
    }

    protected static boolean parseBool(Map<String, String> optionsMap, String key, boolean defaultValue) {
        String value = optionsMap.get(key);
        if (value == null) {
            return defaultValue;
        } else {
            return BooleanUtils.toBoolean(value);
        }
    }

    /**
     * Called to parse a map into a set of specific options.
     *
     * @param options the option map
     */
    protected abstract void parse(Map<String, String> options);
}
