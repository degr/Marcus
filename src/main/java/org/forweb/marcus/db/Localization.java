package org.forweb.marcus.db;

import org.forweb.marcus.Main;
import org.forweb.marcus.dictionaries.db.MySqlDbDictionary;
import org.forweb.marcus.entity.Language;
import org.forweb.marcus.gui.NewPhrase;

import javax.swing.*;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by rsmirnou on 7/31/2015. 09
 */
public class Localization {
    private static Integer languageId;

    public static void setLanguage(Language language) {
        languageId = language.id;
    }

    public static String getPhrase(String key){
        try {
            String out =  DB.getCell(String.class, "select value from phrases where alias = '" + key + "' and language = "+languageId);
            if(out == null) {
                return onUnkonwnPhrase(key);
            } else {
                return out;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String onUnkonwnPhrase(String key) {
        NewPhrase phrase = new NewPhrase(key);
        int answer = JOptionPane.showOptionDialog(Main.frame, phrase, "Undefined phrase", JOptionPane.DEFAULT_OPTION,
                JOptionPane.WARNING_MESSAGE, null, null, null);
        if(answer == 0) {
            DB.query(
                    "insert into phrases (value, alias, language) values (?, ?, ?)",
                    phrase.getValue(),
                    phrase.getAlias(),
                    phrase.getLanguage().toString()
            );
            return phrase.getValue();
        } else {
            return onUnkonwnPhrase(key);
        }
    }

    public static Language getLanguage() {
        for(Map.Entry<String, Language> entry : MySqlDbDictionary.languageMap.entrySet()) {
            if(entry.getValue().id.equals(languageId)) {
                return entry.getValue();
            }
        }
        return null;
    }
}
