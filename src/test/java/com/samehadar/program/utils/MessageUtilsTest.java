package com.samehadar.program.utils;

import org.junit.Test;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.*;

/**
 * Created by User on 18.12.2016.
 */
public class MessageUtilsTest {

    @Test
    public void TestMethodEquals_Md5Custom() {
        String openText = "It's very secret and important message that needs subscribe!";
        MessageDigest messageDigest;
        byte[] digest = new byte[0];

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(openText.getBytes());
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {}

        BigInteger bigInt = new BigInteger(1, digest);
        String md5Hex = bigInt.toString(16);

        while( md5Hex.length() < 32 ){
            md5Hex = "0" + md5Hex;
        }
        assertEquals(bigInt, new BigInteger("265571754159523953380419368439274461446"));
        assertEquals(new BigInteger(md5Hex, 16), new BigInteger("265571754159523953380419368439274461446"));
    }
}