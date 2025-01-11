package com.gamestudio.interfaces;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

public class GameFrame extends JFrame implements Runnable{
    public static final int width = 1000;
    public static final int height = 600;
    private CardLayout cardLayout;
    private JPanel panels;
    StartScreen startScreen;


    public GameFrame() {
        super("Mega man");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Toolkit toolkit = this.getToolkit();
        Dimension desktopSolution = toolkit.getScreenSize();

        startScreen = new StartScreen();

        cardLayout = new CardLayout();
        panels = new JPanel(cardLayout);
        panels.add(startScreen, "Menu");

        addKeyListener(startScreen);
        this.add(panels);
        this.setBounds(
                (desktopSolution.width - width) / 2,
                (desktopSolution.height - height) / 2,
                width,
                height
        );
    }

    public void open() {
        super.setVisible(true);
        new Thread(this).start();
    }

    public void close() {
        super.setVisible(false);
    }

    public void show(String namePanel) {
        cardLayout.show(panels, namePanel);
    }

    @Override
    public void run() {
        long previousTime = System.nanoTime();
        long currentTime;
        long sleepTime;
        long framePeriod = 1000000000 / 30; // 60 FPS

        while (true) {
            // Atualizar a tela atual
            JPanel activePanel = (JPanel) panels.getComponent(0);
            if (activePanel instanceof Runnable runnable) {
                runnable.run();
            }

            // Controle de FPS
            currentTime = System.nanoTime();
            sleepTime = framePeriod - (currentTime - previousTime);
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime / 1000000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            previousTime = System.nanoTime();
        }   

    }

    public static void main(String arg[]){
        GameFrame gameFrame = new GameFrame();
        gameFrame.open();
        gameFrame.show("Menu");
    }
}
