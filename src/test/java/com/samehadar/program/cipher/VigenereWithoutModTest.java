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
        String result = vigenere.encrypt("123", "mes");
        assertEquals("\u009E\u0097¦", result);
        System.out.println(result);
    }

    @Test
    public void Should_TrueDecryptMessage() {
        String openText = vigenere.decrypt("123", "\u009E\u0097¦");
        assertEquals("mes", openText);
        System.out.println(openText);
    }

    @Test
    public void Should_CorrectEncAndDecMessage() {
        String cipherText = vigenere.encrypt("123123456456", "It's very secret and important message!");
        String openText = vigenere.decrypt("123123456456", cipherText);
        assertEquals("It's very secret and important message!", openText);
        System.out.println(openText);
    }

}