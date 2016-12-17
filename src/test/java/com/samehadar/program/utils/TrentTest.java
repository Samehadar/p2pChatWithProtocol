package com.samehadar.program.utils;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by User on 16.12.2016.
 */
public class TrentTest {
    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void Should_CorrectParseMessage() {
        String message = "Alice|anyKey|any|any2";
        List<String> resultParsing = Trent.parseMessage(message);
        assertEquals(Arrays.asList("Alice", "anyKey", "any", "any2"), resultParsing);
        System.out.println(resultParsing);
    }

    @Test
    public void Should_CorrectCreateMessageFromArray() {
        String[] strings = {"Alice", "anyKey", "any", "any2"};
        String result = Trent.createMessage(strings);
        assertEquals("Alice|anyKey|any|any2|", result);
        System.out.println(result);
    }

    @Test
    public void Should_CorrectCreateMessageFromList() {
        List<String> strings = new ArrayList<String>() {{add("Alice");
            add("anyKey"); add("any"); add("any2");}};
        String result = Trent.createMessage(strings);
        assertEquals("Alice|anyKey|any|any2|", result);
        System.out.println(result);
    }

    @Test
    public void Should_CorrectCreateMessageFromListAndArray() {
        List<String> arrayList = new ArrayList<String>() {{add("Alice");
            add("anyKey"); add("any3"); add("any2");}};
        String[] strings = {"Alice", "anyKey", "any1", "any4"};
        String result = Trent.createMessage(arrayList, strings);
        assertEquals("Alice|anyKey|any3|any2|Alice|anyKey|any1|any4|", result);
        System.out.println(result);
    }
}