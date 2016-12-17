package com.samehadar.program.utils;

import org.junit.Test;

/**
 * Created by User on 16.12.2016.
 */
public class DateTimeFormatTest {

    @Test
    public void Should_CorrectConvert() {
        String now = DateTimeFormat.getNowString();
        System.out.println(now);
        System.out.println(DateTimeFormat.parseDate(now));
    }

    @Test
    public void Should_CorrectConvertTimeStamp() {
        String now = DateTimeFormat.getNowTimeStamp();
        System.out.println(now);
        System.out.println(DateTimeFormat.parseTimestamp(now));
    }
}