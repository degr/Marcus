package org.forweb.marcus.gui.builder;

import javax.swing.*;

/**
 * Created by rsmirnou on 8/10/2015. 42
 */
public class StringComponent extends Component<String> {
    public StringComponent(String name, String value){
        super(name);
        input = new JTextField(value, 20);
        postConstruct();
    }
    @Override
    public String getValue() {
        return ((JTextField)input).getText();
    }
}
