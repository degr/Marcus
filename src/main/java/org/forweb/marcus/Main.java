package org.forweb.marcus;

import org.forweb.marcus.db.DB;
import org.forweb.marcus.entity.Word;
import org.forweb.marcus.gui.Gui;
import org.forweb.marcus.gui.GuiBuilder;
import org.forweb.marcus.skills.development.CreateDtoSkill;
import org.forweb.marcus.skills.development.utils.Scope;

import javax.swing.*;

/**
 * Created by rsmirnou on 7/30/2015. 47
 */
public class Main {
    public static JFrame frame;

    public static void main(String ... args) throws Exception {
        CreateDtoSkill sk = new CreateDtoSkill();
        Scope sc = new Scope("java", null, "org.forweb.marcus.entity");
        sk.execute(Word.class, sc);

        if(true) {
            return;
        }
        Word w = new Word();
        w.id = 12;
        w.type = "noun";
        w.language = 2;
        w.word = "animal";
        JPanel p = GuiBuilder.buildGui(Word.class.getDeclaredFields(), Word.class, w);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(p);
        frame.setVisible(true);
        if(true) {
            return;
        }
        DB.init("localhost", "marcus", "root", "admin");
        Marcus marcus = new Marcus();

        Gui gui = new Gui();
        frame = gui;

        marcus.setPort(gui);
        gui.setVisible(true);
        gui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

}

