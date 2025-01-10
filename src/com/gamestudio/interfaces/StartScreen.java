package com.gamestudio.interfaces;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class StartScreen extends JPanel{
    

    public StartScreen() {
    }
    
    @Override
    public void paint(Graphics g) {
        g.setColor(Color.red);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}