package org.forweb.marcus.gui;

import org.forweb.marcus.db.Localization;
import org.forweb.marcus.dictionaries.db.MySqlDbDictionary;

import javax.swing.*;

/**
 * Created by rsmirnou on 7/31/2015. 06
 */
public class NewWord extends JPanel {
    JComboBox languageCombobox;
    JComboBox wordTypeCombobox;
    String word;


    public NewWord(String word){
        languageCombobox = new LanguagesCombobox();
        this.word = word;
        this.wordTypeCombobox = new WordTypeCombobox();


        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        this.setLayout(boxLayout);

        this.add(new JLabel(Localization.getPhrase("new_word_properties") + ": " + word));
        this.add(new JLabel(Localization.getPhrase("select_language")));
        this.add(languageCombobox);

        this.add(new JLabel(Localization.getPhrase("word_type")));
        this.add(this.wordTypeCombobox);
    }

    public Integer getLanguage(){
        return MySqlDbDictionary.getLanguage((String) languageCombobox.getSelectedItem()).id;
    }

    public String getWordType(){
        return (String) wordTypeCombobox.getSelectedItem();
    }

    public String getWord(){
        return word;
    }
}
