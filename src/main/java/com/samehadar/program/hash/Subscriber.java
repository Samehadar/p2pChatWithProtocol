package com.samehadar.program.hash;

/**
 * Interface that returns message digest
 */
public interface Subscriber<TResult, TKey> {

    TResult subscribeMessage(String message, TKey key);
}
