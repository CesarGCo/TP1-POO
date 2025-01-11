package com.gamestudio.interfaces;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Toolkit;
import java.awt.Dimension;
import com.gamestudio.manager.ScreenManager;

public class GameFrame extends JFrame implements Runnable {
    public static final int width = 1000;
    public static final int height = 600;
    private ScreenManager screenManager;

    public GameFrame() {
        super("Mega man");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Toolkit toolkit = this.getToolkit();
        Dimension desktopSolution = toolkit.getScreenSize();


        screenManager = new ScreenManager();
        this.add(screenManager.getPanels());
        addKeyListener(screenManager.getStartScreen());

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

    @Override
    public void run() {
        long previousTime = System.nanoTime();
        long currentTime;
        long sleepTime;
        long framePeriod = 1000000000 / 30; // 60 FPS

        while (true) {
            // Atualizar a tela atual
            JPanel activePanel = (JPanel) screenManager.getPanels().getComponent(0);
            if (activePanel instanceof Runnable runnable) {
                runnable.run();
            }

            screenManager.show();

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

    public static void main(String arg[]) {
        GameFrame gameFrame = new GameFrame();
        gameFrame.open();
    }
}
