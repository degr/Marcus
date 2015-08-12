package org.forweb.marcus.gui;

import org.forweb.marcus.Marcus;
import org.forweb.marcus.db.Localization;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by rsmirnou on 7/30/2015. 48
 */
public class Gui extends JFrame {
    public JTextArea input;
    public JTextArea output;

    public Gui(){
        input = new JTextArea();

        setBounds(20, 20, 600, 300);

        output = new JTextArea();
        JPanel panel = new JPanel();
        BoxLayout bl = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(bl);

        panel.add(new JLabel(Localization.getPhrase("marcus_output")));
        panel.add(new JScrollPane(output));
        panel.add(new JLabel(Localization.getPhrase("marcus_input")));
        panel.add(new JScrollPane(input));
        output.setFocusable(false);
        add(panel);
    }




}
