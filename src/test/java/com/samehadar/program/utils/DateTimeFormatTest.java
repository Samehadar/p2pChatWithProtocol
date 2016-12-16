package com.samehadar.program.utils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by User on 16.12.2016.
 */
public class DateTimeFormatTest {

    @Test
    public void Should_CorrectConvert() {
        String now = DateTimeFormat.getNowString();
        System.out.println(now);
        System.out.println(DateTimeFormat.parseString(now));
    }
}