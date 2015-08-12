package org.forweb.marcus.adapter.gui;

import org.forweb.marcus.Marcus;
import org.forweb.marcus.adapter.Adapter;
import org.forweb.marcus.gui.Gui;

/**
 * Created by rsmirnou on 7/31/2015. 11
 */
public class GuiAdapter implements Adapter<Gui, CharSequence, CharSequence>{
    Gui gui;

    @Override
    public void adapt(Marcus marcus, Gui gui) {
        new KeyListnerImpl(this, gui);
        this.gui = gui;
        gui.input.addKeyListener(new KeyListnerImpl(this, gui));
    }

    @Override
    public boolean isAcceptableForOutput(Object o) {
        return o instanceof CharSequence;
    }

    @Override
    public void output(CharSequence object) {
        gui.output.append(object + "\n");
    }

    @Override
    public boolean isAcceptableForInput(Object o) {
        return false;
    }

    @Override
    public void input(CharSequence object) {
        String out = Marcus.marcus.percept(object instanceof String ? (String)object : object.toString());
        if(out != null && !out.isEmpty()) {
            gui.input.append(out + "\n");
        }
    }
}
