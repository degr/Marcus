package org.forweb.marcus.command;

/**
 * Created by rsmirnou on 7/30/2015. 03
 */
public interface Command<T> {
    <T>T onRequest();
}
