package com.samehadar.program.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.util.Date;

/**
 * Support class for works with date and time
 */
public abstract class DateTimeFormat {

    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
    private static DateTimeFormatter timestamp = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public static String getNowString() {
        return LocalDateTime.now().format(dtf);
    }

    public static String getNowTimeStamp() {
        return LocalDateTime.now().format(timestamp);
    }

    public static TemporalAccessor parseDate(String date) {
        return dtf.parse(date);
    }

    public static TemporalAccessor parseTimestamp(String datetime) {
        return timestamp.parse(datetime);
    }

    public static Long getDeltaWithNowMILLI(String timestamp) {
        TemporalAccessor ts = parseTimestamp(timestamp);
        return ts.getLong(ChronoField.MILLI_OF_SECOND) - LocalDateTime.now().getLong(ChronoField.MILLI_OF_SECOND);
    }

}
