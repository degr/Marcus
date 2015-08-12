package org.forweb.marcus.gui.builder;

import javax.swing.*;
import java.awt.*;

/**
 * Created by rsmirnou on 8/10/2015. 02
 */
public class BooleanComponent extends Component {
    public BooleanComponent(String name, Boolean value) {
        super(name);
        input = new JCheckBox();
        ((JCheckBox)input).setSelected(value != null && value);
        postConstruct();
    }

    @Override
    public Object getValue() {
        return ((JCheckBox)input).isSelected();
    }
}
