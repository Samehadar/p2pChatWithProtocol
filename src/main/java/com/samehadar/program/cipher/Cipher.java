package com.samehadar.program.cipher;

/**
 * Created by User on 14.12.2016.
 */
public interface Cipher<TKey, TResult> {

    TResult encrypt(String openText, TKey key);

    String decrypt(String cipherText, TKey key);
}
