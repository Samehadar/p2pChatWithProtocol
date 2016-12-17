package com.samehadar.program.utils;

import com.samehadar.program.cipher.Cipher;

import java.util.ArrayList;
import java.util.List;

/**
 * Utils for ciphers
 */
public class CipherUtils {

    public static List<String> encryptionForEach(Cipher<String, String> cipher, List<String> strings, String key) {
        List<String> result = new ArrayList<>();
        for (String part : strings) {
            result.add(cipher.encrypt(part, key));
        }
        return result;
    }

    public static List<String> decryptionForEach(Cipher<String, String> cipher, List<String> strings, String key) {
        List<String> result = new ArrayList<>();
        for (String part : strings) {
            result.add(cipher.decrypt(part, key));
        }
        return result;
    }
}
