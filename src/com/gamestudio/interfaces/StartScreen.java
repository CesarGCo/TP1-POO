package com.gamestudio.interfaces;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

import com.gamestudio.state.MenuState;

public class StartScreen extends Screen {
    
    private MenuState menuState;

    public StartScreen() {
        this.menuState = new MenuState(this);
    }
    
    @Override
    public void paint(Graphics g) {
        g.drawImage(menuState.getBufferedImage(), 0, 0, null);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        menuState.setPressedButton(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void run() {
        menuState.render();
        repaint();
    }
}