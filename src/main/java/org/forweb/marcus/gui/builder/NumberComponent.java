package org.forweb.marcus.gui.builder;

import javax.swing.*;

/**
 * Created by rsmirnou on 8/10/2015. 47
 */
public class NumberComponent<T extends Number> extends Component<T> {

    public NumberComponent(String name, T value){
        super(name);
        input = new JSpinner();
        if(value != null) {
            ((JSpinner) input).setValue(value);
        }
        postConstruct();
    }

    @Override
    public T getValue() {
        return (T) ((JSpinner)input).getValue();
    }
}
