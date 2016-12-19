package com.samehadar.program.utils;

/**
 * Interface that returns message digest
 */
//TODO:: change method's name
public interface Subscriber<TResult, TKey> {

    TResult subscribeMessage(String message, TKey key);
}
