package org.forweb.marcus.gui;

import org.forweb.marcus.db.Localization;
import org.forweb.marcus.dictionaries.db.MySqlDbDictionary;
import org.forweb.marcus.entity.Word;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rsmirnou on 7/31/2015. 23
 */
public class NewCommand extends JPanel {
    JTextField commandClassNameInput;
    JTextField wordsInput;
    JTextField attributeInput;

    JPanel tagsPanel;
    JPanel argumentsPanel;

    List<Word> commandTags;
    List<String> commandAttributes;

    public NewCommand(List<Word> words) {
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);

        commandClassNameInput = new JTextField();
        this.add(new Label("command_name"));
        this.add(commandClassNameInput);



        wordsInput = createWordInput();
        this.add(new Label("command_tags"));
        this.add(wordsInput);
        tagsPanel = new JPanel();
        tagsPanel.setLayout(boxLayout);
        this.add(tagsPanel);
        commandTags = words == null ? new ArrayList<>() : words;
        commandTags.stream().forEach(this::displayWord);





    }

    private JTextField createWordInput() {
        JTextField out = new JTextField();
        //out.addKeyListener();
        return out;
    }

    private void displayWord(Word word) {
        tagsPanel.add(new JLabel(word.word+"("+word.type+")"));
    }



}
