package com.samehadar.program.cipher;

import com.samehadar.program.utils.KeyGen;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

/**
 * {@link VigenereWithoutMod} is class, that cans be used how cipher and message subscriber
 *  based on VigenereCipher algorithm without "mod"
 */
public class VigenereWithoutMod implements Cipher<String, String>, KeyGen<String> {

    private Random secureRandom;

    public VigenereWithoutMod() {
        this.secureRandom = new SecureRandom();
    }

    @Override
    public String encrypt(String openText, String key) {
        if (key == null) {
            return openText;
        } else {
            char[] keys = key.toCharArray();
            char[] messageByte = openText.toCharArray();
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < messageByte.length; i++) {
                result.append((char)(messageByte[i] + keys[i % key.length()]));
            }
            return result.toString();
        }
    }

    @Override
    public String decrypt(String cipherText, String key) {
        if (key == null) {
            return cipherText;
        } else {
            char[] keys = key.toCharArray();
            char[] messageByte = cipherText.toCharArray();
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < messageByte.length; i++) {
                result.append((char)(messageByte[i] - keys[i % key.length()]));
            }
            return result.toString();
        }
    }

    @Override
    public String generateKey(Object... args) {
        return new BigInteger((Integer)args[0], this.secureRandom).toString();
    }
}
