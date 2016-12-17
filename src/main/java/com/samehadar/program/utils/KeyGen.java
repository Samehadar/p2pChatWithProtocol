package com.samehadar.program.utils;

/**
 * Interface of KeyGenerator
 */
public interface KeyGen<TKey> {

    /**
     * Generates key
     * @return TKey generated key
     */
    TKey generateKey(Object ... args);
}
