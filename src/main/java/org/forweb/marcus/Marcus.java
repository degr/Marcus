package org.forweb.marcus;


import com.thoughtworks.paranamer.BytecodeReadingParanamer;
import com.thoughtworks.paranamer.Paranamer;
import org.forweb.marcus.adapter.Adapter;
import org.forweb.marcus.adapter.gui.GuiAdapter;
import org.forweb.marcus.command.Command;
import org.forweb.marcus.dictionaries.Dictionary;
import org.forweb.marcus.dictionaries.db.MySqlDbDictionary;
import org.forweb.marcus.entity.Word;
import org.forweb.marcus.gui.Gui;
import org.forweb.marcus.reflection.ReflectUtils;
import org.forweb.marcus.skills.Skill;
import org.forweb.marcus.skills.development.CreateObjectSkill;
import org.forweb.marcus.skills.development.java.JavaClassFinder;
import org.forweb.marcus.skills.development.utils.ClassFinder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by rsmirnou on 7/30/2015. 47
 */
public class Marcus {

    public static final String BASE_PACKAGE = "org.forweb.marcus";

    public static Marcus marcus;

    public static final JavaClassFinder JAVA_CLASS_FINDER = new JavaClassFinder();

    Dictionary dictionary;
    public Context context;
    public Paranamer paranamer = new BytecodeReadingParanamer();
    public CreateObjectSkill createObjectSkill;

    public Marcus() throws IOException {
        dictionary = new MySqlDbDictionary();
        marcus = this;
        createObjectSkill = new CreateObjectSkill(context, paranamer);
    }

    private List<Adapter> adapters = new ArrayList();

    public void setPort(Object port) {
        if(port instanceof Gui) {
            Adapter guiAdapter = new GuiAdapter();
            guiAdapter.adapt(this, port);
            adapters.add(guiAdapter);
        } else {
            System.out.println("I don't know how to work with such kind of port object (" + port.getClass() + ").");
        }
    }

    public String percept(String message) {
        Command command = defineCommand(message);
        System.out.println("in command + "+message);
        return null;
    }

    private Command defineCommand(String message) {
        List<Word> list = Arrays.stream(message.split(" "))
                .filter(v -> v != null && !v.isEmpty())
                .map(this::defineWord)
                .collect(Collectors.toList());

        return null;
    }

    /**
     * Define new word, or create it
     * @param string word value
     * @return Word
     */
    private Word defineWord(String string) {
        string = string.trim();
        Word out = dictionary.getWord(string);
        if(out == null) {
            if(dictionary.onNewWordRequest(string)) {
                dictionary.saveWord(string);
            }
        }
        return out;
    }

    public static void out(String out) {
        for (Adapter adapter : marcus.adapters) {
            if(adapter.isAcceptableForOutput(out)){
                adapter.output(out);
            }
        }
    }

    public static Object invokeSkill(String skillName, Object ... arguments) {
        Class<Skill> skillClass = JAVA_CLASS_FINDER.getClass(BASE_PACKAGE + ".skills", Skill.class, skillName);
        Skill skill = marcus.createObjectSkill.createObject(skillClass);
        return skill.execute(arguments);
    }
}
