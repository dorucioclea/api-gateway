package com.t1t.apim.core.logging;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Simple ISO-8601 format using local TZ.
 *
 */
public class DefaultTimeImpl implements Time {
    private static TimeZone zone = TimeZone.getDefault();
    private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); //$NON-NLS-1$

    static {
        format.setTimeZone(zone);
    }

    @Override
    public String currentTimeIso8601() {
        return format.format(new Date());
    }
}
