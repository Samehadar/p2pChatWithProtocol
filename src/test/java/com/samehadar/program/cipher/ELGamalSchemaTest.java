package com.samehadar.program.cipher;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.support.membermodification.MemberMatcher.method;

/**
 * Tests for ElgamalSchema
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(ELGamalSchema.class)
public class ELGamalSchemaTest {
    @org.junit.Before
    public void setUp() throws Exception {

    }

    //========================START OF ELGAMAL SCHEMA CIPHER

    //For example, p = 11, g = 2, x = 8, k = 9
    @Test
    public void Should_TrueEncryption() throws Exception {
        ELGamalSchema schema = spy(new ELGamalSchema());
        String openText = "5";
        Map<String, BigInteger> keys = schema.generateKey(11, 2, 8);
        BigInteger k = new BigInteger("9");
        when(schema, method(ELGamalSchema.class, "createBigIntegerLowThanP", BigInteger.class)).withArguments(new BigInteger("11")).thenReturn(k);
        Map<String, Integer> ab = schema.encrypt(openText, keys);
        assertEquals(ab.get("a"), new BigInteger("6"));
        assertEquals(ab.get("b"), new BigInteger("9"));
        System.out.println(ab);
    }

    @Test
    public void Should_TrueDecryption() {
        Map<String, BigInteger> ab = new HashMap<String, BigInteger>() {{put("a", new BigInteger("6")); put("b", new BigInteger("9"));}};
        ELGamalSchema schema = new ELGamalSchema();
        Map<String, BigInteger> keys = schema.generateKey(11, 2, 8);
        String openText  = schema.decrypt(schema.concatenateCipherText(ab), keys);
        assertEquals(openText, "5");
        System.out.println(openText);
    }


    @Test
    public void Should_EncAndDecCorrect() throws Exception {
        ELGamalSchema schema = spy(new ELGamalSchema());
        BigInteger p = new BigInteger("2539");
        BigInteger g = new BigInteger("490");
        String openText = "81";
        System.out.println("openText = " + openText);
        BigInteger x = new BigInteger("193");
        System.out.println("x = " + x);
        Map<String, BigInteger> keys = schema.generateKey(p, g, x);
        System.out.println(keys);
        BigInteger k = new BigInteger("171");
        System.out.println("k = " + k);
        when(schema, method(ELGamalSchema.class, "createBigIntegerLowThanP", BigInteger.class)).withArguments(p).thenReturn(k);
        Map<String, BigInteger> ab = schema.encrypt(openText, keys);
        System.out.println(ab);
        String decrText = schema.decrypt(schema.concatenateCipherText(ab), keys);
        assertEquals(decrText, openText);
        System.out.println(decrText + "~" + openText);
    }

    @Test
    public void Should_EncAndDecCorrect_onRandomBigIntegers() throws Exception {
        ELGamalSchema schema = spy(new ELGamalSchema());
        BigInteger p, g, x, k;
        Random sc = new SecureRandom();
        p = BigInteger.probablePrime(64, sc);
        g = new BigInteger("3");
        String openText = "123125125";
        System.out.println("openText = " + openText);
        x = new BigInteger("12345678901234567890");
        System.out.println("x = " + x);
        Map<String, BigInteger> keys = schema.generateKey(p, g, x);
        System.out.println(keys);
        k = new BigInteger(64, sc);
        System.out.println("k = " + k);
        when(schema, method(ELGamalSchema.class, "createBigIntegerLowThanP", BigInteger.class)).withArguments(p).thenReturn(k);
        Map<String, BigInteger> ab = schema.encrypt(openText, keys);
        System.out.println(ab);
        String decrText = schema.decrypt(schema.concatenateCipherText(ab), keys);
        assertEquals(decrText, openText);
        System.out.println(decrText + "~" + openText);
    }
    //========================END OF ELGAMAL SCHEMA CIPHER

    //========================START OF ELGAMAML SCHEMA SUBSCRIBER
    @Test
    public void Should_SubscribeMessage_WithoutRandomKeys() throws Exception {
        String message = "It's very secret and important message that needs subscribe!";
        ELGamalSchema schema = spy(new ELGamalSchema());
        BigInteger p = new BigInteger("2539");
        BigInteger g = new BigInteger("490");
        String openText = "81";
        System.out.println("openText = " + openText);
        BigInteger x = new BigInteger("193");
        System.out.println("x = " + x);
        Map<String, BigInteger> keys = schema.generateKey(p, g, x);
        System.out.println(keys);
        BigInteger k = new BigInteger("171");
        System.out.println("k = " + k);
        when(schema, method(ELGamalSchema.class, "createBigIntegerLowThanP", BigInteger.class)).withArguments(p).thenReturn(k);
        Map<String, String> result = schema.subscribeMessage(message, keys);
        System.out.println(result);
        assertEquals(result.get("r"), "190");
        assertEquals(result.get("s"), "2332");
    }

}