package org.forweb.marcus.gui;

import org.forweb.marcus.db.Localization;
import org.forweb.marcus.dictionaries.db.MySqlDbDictionary;

import javax.swing.*;

/**
 * Created by rsmirnou on 7/31/2015. 23
 */
public class NewPhrase extends JPanel {
    JComboBox languageCombobox;
    JTextField valueInput;
    String key;

    public NewPhrase(String key) {
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        this.key = key;
        languageCombobox = new LanguagesCombobox();
        valueInput = new JTextField();

        this.setLayout(boxLayout);
        this.add(new JLabel("Select language: "));
        this.add(languageCombobox);
        this.add(new JLabel("Enter value for key - `" +key+"`"));
        this.add(valueInput);

    }

    public String getAlias(){
        return key;
    }
    public String getValue(){
        return valueInput.getText();
    }
    public Integer getLanguage(){
        return MySqlDbDictionary.getLanguage((String) languageCombobox.getSelectedItem()).id;
    }


}
