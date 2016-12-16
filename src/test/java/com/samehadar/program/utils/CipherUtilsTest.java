package com.samehadar.program.utils;

import com.samehadar.program.cipher.CesarWithoutMod;
import com.samehadar.program.cipher.Cipher;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by User on 16.12.2016.
 */
public class CipherUtilsTest {

    @Test
    public void Should_CipherForEachString() {
        Cipher<String, String> cesar = new CesarWithoutMod();
        String key = "123";
        List<String> strings = Arrays.asList("bdf", "\u0092 ¬", "\u0092 ¬", "\u0092 ¬");
        List<String> result = CipherUtils.cipherForEach(cesar, Arrays.asList("123", "anyKey", "any", "any2"), key);
        assertEquals(strings, result);
        System.out.println(result);
    }

}