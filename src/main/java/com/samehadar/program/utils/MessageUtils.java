package com.samehadar.program.utils;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

/**
 * Created by User on 18.12.2016.
 */
public class MessageUtils {

    //can be replaced by Apache Common Codec in future
    public static String md5Custom(String message) {
        java.security.MessageDigest messageDigest;
        byte[] digest = new byte[0];

        try {
            messageDigest = java.security.MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(message.getBytes());
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            // TODO:: exception trowed when algorithm in getInstance(...) is not exist
            e.printStackTrace();
        }

        BigInteger bigInt = new BigInteger(1, digest);
        String md5Hex = bigInt.toString(16);

        while( md5Hex.length() < 32 ){
            md5Hex = "0" + md5Hex;
        }
        return md5Hex;
    }
}
