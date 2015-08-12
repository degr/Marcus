package org.forweb.marcus.adapter.gui;

import org.forweb.marcus.Marcus;
import org.forweb.marcus.gui.Gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by rsmirnou on 7/31/2015. 21
 */
public class KeyListnerImpl implements KeyListener {
    boolean ctrl = false;
    boolean enter = false;
    private GuiAdapter adapter;
    private Gui gui;

    public KeyListnerImpl(GuiAdapter adapter, Gui gui) {
        this.adapter = adapter;
        this.gui = gui;
    }


    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
            ctrl = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            enter = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
            if (enter) {
                sendRequest();
            }
            ctrl = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (ctrl) {
                sendRequest();
            }
            enter = false;
        }
    }

    private void sendRequest() {
        adapter.input(gui.input.getText());
        gui.input.setText("");
    }
}
