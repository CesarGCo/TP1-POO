package com.gamestudio.interfaces;

import java.awt.Graphics;

import javax.swing.JPanel;

import com.gamestudio.state.MenuState;

public class StartScreen extends JPanel{
    
    private MenuState menuState;

    public StartScreen() {
        this.menuState = new MenuState(this);
    }
    
    @Override
    public void paint(Graphics g) {
        g.drawImage(menuState.getBufferedImage(), 0, 0, null);
        menuState.render();
    }
}