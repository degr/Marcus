package org.forweb.marcus.adapter;

import org.forweb.marcus.Marcus;

/**
 * Created by rsmirnou on 7/31/2015. 11
 */
public interface Adapter<T, O, I> {
    void adapt(Marcus marcus, T object);

    boolean isAcceptableForOutput(Object o);
    void output(O object);

    boolean isAcceptableForInput(Object o);
    void input(I object);
}
