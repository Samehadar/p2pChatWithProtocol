package com.samehadar.program.cipher;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.math.BigInteger;
import java.security.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.support.membermodification.MemberMatcher.method;

/**
 * Created by User on 15.12.2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(ElgamalShema.class)
public class ElgamalShemaTest {
    @org.junit.Before
    public void setUp() throws Exception {

    }

    //========================ElgamalSchema

    //For example, p = 11, g = 2, x = 8, k = 9
    @Test
    public void Should_TrueEncryption() throws Exception {
        ElgamalShema schema = spy(new ElgamalShema());
        String openText = "5";
        Map<String, Long> keys = schema.generateKey(11, 2, 8);
        Integer k = 9;
        when(schema, method(ElgamalShema.class, "createIntegerLowThanP", Integer.class)).withArguments(anyInt()).thenReturn(k);
        Map<String, Integer> ab = schema.encrypt(openText, keys);
        assertEquals(ab.get("a"), (Integer)6);
        assertEquals(ab.get("b"), (Integer)9);
        System.out.println(ab);
    }

    @Test
    public void Should_TrueDecryption() {
        Map<String, Integer> ab = new HashMap<String, Integer>() {{put("a", 6); put("b", 9);}};
        ElgamalShema schema = new ElgamalShema();
        Map<String, Integer> keys = schema.generateKey(11, 2, 8);
        String openText  = schema.decrypt(schema.concatenateCipherText(ab), keys);
        assertEquals(openText, "5");
        System.out.println(openText);
    }

    @Test
    public void Should_EncAndDecCorrect() throws Exception {
        ElgamalShema schema = spy(new ElgamalShema());
        String openText = "5";
        Map<String, Integer> keys = schema.generateKey(11, 2, 8);
        Integer k = 9;
        when(schema, method(ElgamalShema.class, "createIntegerLowThanP", Integer.class)).withArguments(anyInt()).thenReturn(k);
        Map<String, Integer> ab = schema.encrypt(openText, keys);
        assertEquals(ab.get("a"), (Integer)6);
        assertEquals(ab.get("b"), (Integer)9);
        assertEquals(ab, ab);
        System.out.println(ab);
        System.out.println(ab);
        String decrText = schema.decrypt(schema.concatenateCipherText(ab), keys);
        assertEquals(decrText, openText);
        System.out.println(openText + "~" + decrText);
    }

    @Test
    public void Should_EncAndDecCorrect_OnBigInteger() throws Exception {
        ElgamalShema schema = spy(new ElgamalShema());
        //String openText = BigInteger.probablePrime(11, new Random(33)).toString();
        String openText = "53";
        System.out.println("openText = " + openText);
        //Integer x = (int)(Math.random() * 10000000);
        Integer x = 133;
        System.out.println("x = " + x);
        Map<String, Integer> keys = schema.generateKey(6883, 5221, x);
        System.out.println(keys);
        //Integer k = (int)(Math.random() * 10000000);
        Integer k = 110;
        System.out.println("k = " + k);
        when(schema, method(ElgamalShema.class, "createIntegerLowThanP", Integer.class)).withArguments(anyInt()).thenReturn(k);
        Map<String, Integer> ab = schema.encrypt(openText, keys);
        System.out.println(ab);
        String decrText = schema.decrypt(schema.concatenateCipherText(ab), keys);
        assertEquals(decrText, openText);
        System.out.println(decrText + "~" + openText);
    }

    @Test
    public void Should_EncAndDecCorrect_OnBigInteger2() throws Exception {
        ElgamalShema schema = spy(new ElgamalShema());
        String openText = String.valueOf(133);
        System.out.println("openText = " + openText);
        Integer x = 151;
        System.out.println("x = " + x);
        Map<String, Integer> keys = schema.generateKey(6037, 556, x);
        System.out.println(keys);
        Integer k = 189;
        System.out.println("k = " + k);
        when(schema, method(ElgamalShema.class, "createIntegerLowThanP", Integer.class)).withArguments(anyInt()).thenReturn(k);
        Map<String, Integer> ab = schema.encrypt(openText, keys);
        System.out.println(ab);
        String decrText = schema.decrypt(schema.concatenateCipherText(ab), keys);
        assertEquals(decrText, openText);
        System.out.println(decrText + "~" + openText);
    }

    @Test
    public void Should_EncAndDecCorrect_OnRandomXandK() throws Exception {
        ElgamalShema schema = spy(new ElgamalShema());
        String openText = BigInteger.probablePrime(11, new Random(33)).toString();
        System.out.println("openText = " + openText);
        Integer x = (int)(Math.random() * 6883);
        System.out.println("x = " + x);
        Map<String, Integer> keys = schema.generateKey(6883, 5221, x);
        System.out.println(keys);
        Integer k = (int)(Math.random() * 6882);
        System.out.println("k = " + k);
        when(schema, method(ElgamalShema.class, "createIntegerLowThanP", Integer.class)).withArguments(anyInt()).thenReturn(k);
        Map<String, Integer> ab = schema.encrypt(openText, keys);
        System.out.println(ab);
        String decrText = schema.decrypt(schema.concatenateCipherText(ab), keys);
        assertEquals(decrText, openText);
        System.out.println(decrText + "~" + openText);
    }

    @Test
    public void xxx() {
        BigInteger p, b, c, secretKey;
        Random sc = new SecureRandom();
        secretKey = new BigInteger("12345678901234567890");
        //
        // public key calculation
        //
        System.out.println("secretKey = " + secretKey);
        p = BigInteger.probablePrime(64, sc);
        //g
        b = new BigInteger("3");
        //x
        c = b.modPow(secretKey, p);
        System.out.println("p = " + p);
        System.out.println("b = " + b);
        System.out.println("c = " + c);
        //
        // Encryption
        //
        System.out.print("Enter your Big Number message -->");
        String s = String.valueOf(123125125);
        BigInteger X = new BigInteger(s);
        //k
        BigInteger r = new BigInteger(64, sc);
        //M * y^k mod p
        BigInteger EC = X.multiply(c.modPow(r, p)).mod(p);
        //g^k mod p
        BigInteger brmodp = b.modPow(r, p);
        System.out.println("Plaintext = " + X);
        System.out.println("r = " + r);
        System.out.println("EC = " + EC);
        System.out.println("b^r mod p = " + brmodp);
        //
        // Decryption
        //
        BigInteger crmodp = brmodp.modPow(secretKey, p);
        BigInteger d = crmodp.modInverse(p);
        BigInteger ad = d.multiply(EC).mod(p);
        System.out.println("\n\nc^r mod p = " + crmodp);
        System.out.println("d = " + d);
        System.out.println("Alice decodes: " + ad);
    }

    @Test
    public void Should_TrueEncryption_big() throws Exception {
        BigInteger p, b, c, secretKey;
        Random sc = new SecureRandom();
        secretKey = new BigInteger("12345678901234567890");
        //
        // public key calculation
        //
        System.out.println("secretKey = " + secretKey);
        p = BigInteger.probablePrime(64, sc);
        //g
        b = new BigInteger("3");
        //x
        c = b.modPow(secretKey, p);
        System.out.println("p = " + p);
        System.out.println("b = " + b);
        System.out.println("c = " + c);
        //
        // Encryption
        //
        System.out.print("Enter your Big Number message -->");
        String s = String.valueOf(123125125);
        BigInteger X = new BigInteger(s);
        //k
        BigInteger r = new BigInteger(64, sc);
        //M * y^k mod p
        BigInteger EC = X.multiply(c.modPow(r, p)).mod(p);
        //g^k mod p
        BigInteger brmodp = b.modPow(r, p);
        System.out.println("Plaintext = " + X);
        System.out.println("r = " + r);
        System.out.println("EC = " + EC);
        System.out.println("b^r mod p = " + brmodp);

        ElgamalShema schema = spy(new ElgamalShema());
        String openText = s;
        Map<String, Integer> keys = schema.generateKey(12345678901234567890, 3, 8);
        Integer k = 9;
        when(schema, method(ElgamalShema.class, "createIntegerLowThanP", Integer.class)).withArguments(anyInt()).thenReturn(k);
        Map<String, Integer> ab = schema.encrypt(openText, keys);
        assertEquals(ab.get("a"), (Integer)6);
        assertEquals(ab.get("b"), (Integer)9);
        System.out.println(ab);
    }
}