package org.forweb.marcus.gui;

import org.forweb.marcus.dictionaries.db.MySqlDbDictionary;
import org.forweb.marcus.entity.Language;

import javax.swing.*;
import java.util.List;

/**
 * Created by rsmirnou on 7/31/2015. 33
 */
public class LanguagesCombobox extends JComboBox<LanguagesCombobox.ToComboBoxModel>{
    public LanguagesCombobox(){
        this.setModel(new ToComboBoxModel(MySqlDbDictionary.getLanguages()));
        setSelectedIndex(0);
    }

    public class ToComboBoxModel extends AbstractListModel implements ComboBoxModel {
        private String selectedItem;

        private List<Language> languages;

        public ToComboBoxModel(List<Language> orgs) {
            this.languages = orgs;
        }

        @Override
        public String getSelectedItem() {
            return selectedItem;
        }

        @Override
        public void setSelectedItem(Object newValue) {
            for (Language o: languages){
                if (newValue.equals(o.key)){
                    selectedItem=o.key;
                    break;
                }
            }
        }

        @Override
        public int getSize() {
            return languages.size();
        }

        @Override
        public String getElementAt(int i) {
            return languages.get(i).key;
        }
    }
}
