package com.samehadar.program.utils;

/**
 * Interface that returns message digest
 */
public interface Subscriber<TResult, TKey> {

    TResult subscribeMessage(String message, TKey key);
}
