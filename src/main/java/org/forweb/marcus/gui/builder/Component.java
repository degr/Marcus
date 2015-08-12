package org.forweb.marcus.gui.builder;

import sun.plugin2.gluegen.runtime.CPU;

import javax.swing.*;
import java.awt.*;

/**
 * Created by rsmirnou on 8/10/2015. 40
 */
public abstract class Component<T> extends JPanel {
    private static final Dimension MINIMUM_SIZE = new Dimension(50, 22);

    public abstract T getValue();
    JComponent input;
    public Component(String name){
        GridLayout gl = new GridLayout(2, 1);
        setLayout(gl);
        this.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, false));
        add(new JLabel(name + ": "));
    }

    protected void postConstruct(){
        input.setMinimumSize(MINIMUM_SIZE);
        input.setSize(MINIMUM_SIZE);
        add(input);
    }
}
