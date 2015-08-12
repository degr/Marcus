package org.forweb.marcus.gui;

import org.forweb.marcus.db.Localization;

import javax.swing.*;

/**
 * Created by rsmirnou on 8/4/2015. 50
 */
public class Label extends JLabel {
    public Label(String key) {
        super(Localization.getPhrase(key));
    }
}
