package com.gamestudio.interfaces;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

import com.gamestudio.state.State;

public class Screen extends JPanel implements Runnable, KeyListener{
    private State state;
    Thread gameThread;

    public Screen(State state) {
        this.state = state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void startGame(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(state.getBufferedImage(), 0, 0, null);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        this.state.setPressedButton(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        this.state.setReleasedButton(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void run() {

        long previousTime = System.nanoTime();
        long currentTime;
        long sleepTime;

        long period = 1000000000/80;

        while(true){

            state.update();
            state.render();
            repaint();

            currentTime = System.nanoTime();
            sleepTime = period - (currentTime - previousTime);
            try{

                    if(sleepTime > 0)
                            Thread.sleep(sleepTime/1000000);
                    else Thread.sleep(period/2000000);

            }catch(Exception e){}

            previousTime = System.nanoTime();
        }

    }
}
