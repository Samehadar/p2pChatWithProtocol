package com.samehadar.program.cipher;

/**
 * TODO:: add comments
 */
public class CesarWithoutMod implements Cipher<String, String> {

    public CesarWithoutMod() {}

    @Override
    public String encrypt(String key, String message) {
        if (key == null) {
            return message;
        } else {
            char[] keys = key.toCharArray();
            char[] messageByte = message.toCharArray();
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < messageByte.length; i++) {
                result.append((char)(messageByte[i] + keys[i % key.length()]));
            }
            return result.toString();
        }
    }

    @Override
    public String decrypt(String key, String encryptedMessage) {
        if (key == null) {
            return encryptedMessage;
        } else {
            char[] keys = key.toCharArray();
            char[] messageByte = encryptedMessage.toCharArray();
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < messageByte.length; i++) {
                result.append((char)(messageByte[i] - keys[i % key.length()]));
            }
            return result.toString();
        }
    }

}
