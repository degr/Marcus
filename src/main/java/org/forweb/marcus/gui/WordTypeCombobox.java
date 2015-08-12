package org.forweb.marcus.gui;

import org.forweb.marcus.db.DB;

import javax.swing.*;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by rsmirnou on 8/3/2015. 27
 */
public class WordTypeCombobox extends JComboBox<String> {
    private static List<String> wordTypes;
    public WordTypeCombobox(){
        if(wordTypes == null) {
            initWordTypes();
        }
        for(String type : wordTypes) {
            addItem(type);
        }
    }

    private void initWordTypes() {
        try {
            List<Map> table = DB.getList(Map.class, "SHOW COLUMNS FROM words WHERE Field = 'type'");
            for(Map map : table) {
                String enumS = (String) map.get("COLUMN_TYPE");
                String[] enumValues = enumS.substring(enumS.indexOf("('") + 2, enumS.lastIndexOf("')")).split("','");
                wordTypes = Arrays.asList(enumValues);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
