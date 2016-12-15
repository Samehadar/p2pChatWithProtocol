package com.samehadar.program.cipher;

/**
 * Created by User on 14.12.2016.
 */
public interface KeyGen<TKey> {

    /**
     * Generates key
     * @return TKey generated key
     */
    TKey generateKey(Object ... args);
}