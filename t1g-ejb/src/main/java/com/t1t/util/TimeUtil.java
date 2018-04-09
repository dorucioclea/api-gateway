package com.t1t.util;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public final class TimeUtil {

    private static final String NEGATIVE_TIME = "Congratulations, you've proven time-travel is possible!";

    private TimeUtil() {}

    public static String getTimeSince(LocalDateTime start) {
        Duration duration = getDurationSince(start);
        if (!duration.isNegative()) {
            Long seconds = duration.getSeconds();
            return String.format(
                    "%02dh:%02dm:%02ds",
                    seconds / 3600,
                    (seconds % 3600) / 60,
                    seconds % 60);
        } else {
            return NEGATIVE_TIME;
        }
    }

    public static Duration getDurationSince(LocalDateTime start) {
        return Duration.between(start, LocalDateTime.now());
    }

    public static String convertDateTimeToSecondsString(DateTime time) {
        String rval = null;
        if (time != null) {
            rval = String.valueOf(time.getMillis() / 1000);
        }
        return rval;
    }

    public static String getFormattedDateTime(DateTime dateTime) {
        return dateTime.toString("yyyy/MM/dd HH:mm:ss");
    }

    public static String getFormattedIntervalBetweenThenAndNow(DateTime start) {
        DateTime now = DateTime.now();
        int years = Years.yearsBetween(start, now).getYears();
        now = now.minusYears(years);
        int months = Months.monthsBetween(start, now).getMonths();
        now = now.minusMonths(months);
        int weeks = Weeks.weeksBetween(start, now).getWeeks();
        now = now.minusWeeks(weeks);
        int days = Days.daysBetween(start, now).getDays();
        now = now.minusDays(days);
        int hours = Hours.hoursBetween(start, now).getHours();
        now = now.minusHours(hours);
        int minutes = Minutes.minutesBetween(start, now).getMinutes();
        now = now.minusMinutes(minutes);
        int seconds = Seconds.secondsBetween(start, now).getSeconds();
        List<String> formattedTimeIntervals = getFormattedTimeInterval(years, months, weeks, days, hours, minutes, seconds);
        Iterator<String> it = formattedTimeIntervals.iterator();
        StringBuilder sb = new StringBuilder("");
        while (it.hasNext()) {
            String next = it.next();
            if (StringUtils.isNotEmpty(sb.toString()) && StringUtils.isNotEmpty(next)) {
                sb.append(", ");
            }
            sb.append(next);
        }
        return sb.toString();
    }

    public static DateTime convertBuildTimeToDateTime(String buildTime) {
        if (StringUtils.isNotEmpty(buildTime)) {
            return new DateTime(buildTime, DateTimeZone.UTC).withZone(DateTimeZone.getDefault());
        }
        return null;
    }

    private static List<String> getFormattedTimeInterval(int years, int months, int weeks, int days, int hours, int minutes, int seconds) {
        List<String> rval = new ArrayList<>();
        rval.add(formatTimeInterval(years, " year"));
        rval.add(formatTimeInterval(months, " month"));
        rval.add(formatTimeInterval(weeks, " week"));
        rval.add(formatTimeInterval(days, " day"));
        rval.add(formatTimeInterval(hours, " hour"));
        rval.add(formatTimeInterval(minutes, " minute"));
        rval.add(formatTimeInterval(seconds, " second"));
        return rval;
    }

    private static String formatTimeInterval(int time, String unit) {
        StringBuilder sb = new StringBuilder("");
        if (time > 0) {
            sb.append(time).append(unit);
            if (time > 1) {
                sb.append("s");
            }
        }
        return sb.toString();
    }
}