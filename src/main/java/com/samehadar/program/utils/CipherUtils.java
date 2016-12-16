package com.samehadar.program.utils;

import com.samehadar.program.cipher.Cipher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 16.12.2016.
 */
public class CipherUtils {

    public static List<String> cipherForEach(Cipher<String, String> cipher, List<String> strings, String key) {
        List<String> result = new ArrayList<>();
        for (String part : strings) {
            result.add(cipher.encrypt(part, key));
        }
        return result;
    }
}
