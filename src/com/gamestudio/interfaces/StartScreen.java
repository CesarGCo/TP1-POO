package com.gamestudio.interfaces;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import com.gamestudio.state.State;

public class StartScreen extends Screen {

    public StartScreen(State state) {
        super(state);
    }
    
    @Override
    public void paint(Graphics g) {
        g.drawImage(getState().getBufferedImage(), 0, 0, null);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        getState().setPressedButton(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void run() {
        getState().render();
        repaint();
    }
}