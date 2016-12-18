package com.samehadar.program.cipher;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by User on 16.12.2016.
 */
public class VigenereWithoutModTest {
    VigenereWithoutMod vigenere;

    @Before
    public void setUp() throws Exception {
        vigenere = new VigenereWithoutMod();
    }

    @Test
    public void Should_TrueEncryptMessage() {
        String result = vigenere.encrypt("mes", "123");
        assertEquals("\u009E\u0097¦", result);
        System.out.println(result);
    }

    @Test
    public void Should_TrueDecryptMessage() {
        String openText = vigenere.decrypt("\u009E\u0097¦", "123");
        assertEquals("mes", openText);
        System.out.println(openText);
    }

    @Test
    public void Should_CorrectEncAndDecMessage() {
        String cipherText = vigenere.encrypt("It's very secret and important message!", "123123456456");
        String openText = vigenere.decrypt(cipherText, "123123456456");
        assertEquals("It's very secret and important message!", openText);
        System.out.println(openText);
    }

}