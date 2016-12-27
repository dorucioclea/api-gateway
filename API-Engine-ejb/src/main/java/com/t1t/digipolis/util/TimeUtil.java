package com.t1t.digipolis.util;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class TimeUtil {

    private static final String NEGATIVE_TIME = "Congratulations, you've proven time-travel is possible!";

    public static String getTimeSince(LocalDateTime start) {
        Duration duration = getDurationSince(start);
        if (!duration.isNegative()) {
            Long seconds = duration.getSeconds();
            return String.format(
                    "%02dh:%02dm:%02ds",
                    seconds / 3600,
                    (seconds % 3600) / 60,
                    seconds % 60);
        }
        else {
            return NEGATIVE_TIME;
        }
    }

    public static Duration getDurationSince(LocalDateTime start)  {
        return Duration.between(start, LocalDateTime.now());
    }

}