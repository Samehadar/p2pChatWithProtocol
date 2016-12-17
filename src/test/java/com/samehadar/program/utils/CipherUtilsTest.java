package com.samehadar.program.utils;

import com.samehadar.program.cipher.VigenereWithoutMod;
import com.samehadar.program.cipher.Cipher;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class CipherUtilsTest {

    Cipher<String, String> vigenere;

    @Before
    public void before() {
        vigenere = new VigenereWithoutMod();
    }

    @Test
    public void Should_CipherForEachString() {
        vigenere = new VigenereWithoutMod();
        String key = "123";
        List<String> strings = Arrays.asList("bdf", "\u0092 ¬", "\u0092 ¬", "\u0092 ¬");
        List<String> result = CipherUtils.encryptionForEach(vigenere, Arrays.asList("123", "anyKey", "any", "any2"), key);
        assertEquals(strings, result);
        System.out.println(result);
    }

    @Test
    public void s() {
        String openText = "Vitaly|29090977|";
        List<String> parse1 = Trent.parseMessage(openText);
        List<String> result1 = CipherUtils.encryptionForEach(vigenere, parse1, "708570865");

        List<String> result2 = CipherUtils.encryptionForEach(vigenere, Trent.parseMessage(openText), "708570865");

        assertEquals(result1, result2);
        System.out.println(result1);
        System.out.println(result2);
    }

}