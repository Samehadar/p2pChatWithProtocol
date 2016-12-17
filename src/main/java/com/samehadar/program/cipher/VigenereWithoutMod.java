package com.samehadar.program.cipher;

/**
 * TODO:: add comments
 */
public class VigenereWithoutMod implements Cipher<String, String> {

    public VigenereWithoutMod() {}

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

}
