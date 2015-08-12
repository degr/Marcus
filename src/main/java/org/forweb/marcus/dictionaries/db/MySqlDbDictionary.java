package org.forweb.marcus.dictionaries.db;

import org.forweb.marcus.Main;
import org.forweb.marcus.db.DB;
import org.forweb.marcus.db.Localization;
import org.forweb.marcus.dictionaries.Dictionary;
import org.forweb.marcus.entity.Language;
import org.forweb.marcus.entity.Word;
import org.forweb.marcus.gui.NewWord;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rsmirnou on 7/31/2015. 04
 */
public class MySqlDbDictionary implements Dictionary {

    public static Map<String, Language> languageMap = null;

    public static List<Language> getLanguages(){
        List<Language> out = new ArrayList<>();
        for(Map.Entry<String, Language> entry : languageMap.entrySet()) {
            out.add(entry.getValue());
        }
        return out;
    }

    public static Language getLanguage(String name) {
        return languageMap.get(name);
    }

    public MySqlDbDictionary(){
        if(languageMap == null) {
            initLanguages();
        }
    }



    private void initLanguages() {
        try {
            List<Language> list = DB.getList(Language.class);
            languageMap = new HashMap<>(list.size());

            list.stream().forEach(v -> {
                if(v.primary != null && v.primary == 1){
                    Localization.setLanguage(v);
                }
                languageMap.put(v.key, v);
            });
            if(Localization.getLanguage() == null && !list.isEmpty()) {
                Localization.setLanguage(list.get(0));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Word getWord(String str) {
        try {

            return DB.getRow(Word.class, "word", str.trim());
        } catch (SQLException e) {
            System.out.println("Unknown word: " + str);
        }
        return null;
    }

    public boolean saveWord(String word) {
        NewWord wordUi = new NewWord(word.trim());
        int answer = JOptionPane.showOptionDialog(Main.frame, wordUi, "Undefined word", JOptionPane.DEFAULT_OPTION,
                JOptionPane.WARNING_MESSAGE, null, null, null);
        if(answer == 0) {
            DB.query(
                    "insert into words (type, language, word) values (?, ?, ?)",
                    wordUi.getWordType(),
                    wordUi.getLanguage().toString(),
                    wordUi.getWord()
            );

            return true;
        } else {
            return saveWord(word);
        }
    }

    @Override
    public boolean onNewWordRequest(String word) {
        int answer = JOptionPane.showConfirmDialog(Main.frame, Localization.getPhrase("is_new_word") + "["+word+"]", null, JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE);
        return answer == 0;
    }

}
