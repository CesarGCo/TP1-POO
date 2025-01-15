package com.gamestudio.interfaces;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Toolkit;
import java.io.IOException;
import java.awt.Dimension;

import com.gamestudio.manager.DataLoader;
import com.gamestudio.manager.StateManager;

public class GameFrame extends JFrame {
    public static final int width = 1000;
    public static final int height = 600;
    private StateManager stateManager; 


    public GameFrame() {
        super("Mega man");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Toolkit toolkit = this.getToolkit();
        Dimension desktopSolution = toolkit.getScreenSize();
        /*try {
            DataLoader.getInstance().LoadData();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        this.stateManager = new StateManager();

        addKeyListener(stateManager.getScreen());
        add(stateManager.getScreen());
        
        
        this.setBounds(
            (desktopSolution.width - width) / 2,
            (desktopSolution.height - height) / 2,
            width,
            height
        );
    }

    public void open() {
        stateManager.getScreen().startGame();
        this.setVisible(true);
    }

    public static void main(String arg[]) {
        GameFrame gameFrame = new GameFrame();
        gameFrame.open();
    }
}
