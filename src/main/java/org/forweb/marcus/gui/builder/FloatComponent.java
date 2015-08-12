package org.forweb.marcus.gui.builder;

import javax.swing.*;

/**
 * Created by rsmirnou on 8/10/2015. 57
 */
public class FloatComponent<T extends Number> extends NumberComponent<T> {
    public FloatComponent(String name, T value) {
        super(name, value);
    }

    @Override
    public T getValue() {
        return (T)((JSpinner)input).getValue();
    }
}
