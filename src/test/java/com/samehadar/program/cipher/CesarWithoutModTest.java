package com.samehadar.program.cipher;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by User on 16.12.2016.
 */
public class CesarWithoutModTest {
    CesarWithoutMod cesar;

    @Before
    public void setUp() throws Exception {
        cesar = new CesarWithoutMod();
    }

    @Test
    public void Should_TrueEncryptMessage() {
        String result = cesar.encrypt("123", "mes");
        assertEquals("\u009E\u0097¦", result);
        System.out.println(result);
    }

    @Test
    public void Should_TrueDecryptMessage() {
        String openText = cesar.decrypt("123", "\u009E\u0097¦");
        assertEquals("mes", openText);
        System.out.println(openText);
    }

    @Test
    public void Should_CorrectEncAndDecMessage() {
        String cipherText = cesar.encrypt("123123456456", "It's very secret and important message!");
        String openText = cesar.decrypt("123123456456", cipherText);
        assertEquals("It's very secret and important message!", openText);
        System.out.println(openText);
    }

}