package com.samehadar.program.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

/**
 * Created by User on 16.12.2016.
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

}
