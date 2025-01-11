package com.gamestudio.interfaces;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

public abstract class Screen extends JPanel implements Runnable, KeyListener{

    @Override
    public abstract void paint(Graphics g);

    @Override
    public abstract void run();

    @Override
    public abstract void keyPressed(KeyEvent e);

    @Override
    public abstract void keyReleased(KeyEvent e);

    @Override
    public  abstract void keyTyped(KeyEvent e);
}
