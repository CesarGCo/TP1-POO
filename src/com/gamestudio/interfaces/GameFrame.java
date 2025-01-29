package com.gamestudio.interfaces;

import javax.swing.JFrame;
import java.awt.Toolkit;
import java.io.IOException;
import java.awt.Dimension;

import com.gamestudio.manager.DataLoader;
import com.gamestudio.manager.StateManager;


// A classe GameFrame é responsável por representar a janela do Jogo
// Ele representa uma janela com barra de título, bordas e botões padrão
public class GameFrame extends JFrame {
    public static final int width = 1000;
    public static final int height = 600;
    private final StateManager stateManager;

    public GameFrame() {
        //Adicionando títula à janela:
        super("Mega man");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Toolkit toolkit = this.getToolkit();
        Dimension desktopSolution = toolkit.getScreenSize();

        // Adicionando todos os Assets do projeto:
        try {
            DataLoader.getInstance().LoadData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.stateManager = new StateManager();

        //Adicionando ouvinte de eventos de teclado:
        addKeyListener(stateManager.getScreen());
        add(stateManager.getScreen());
        
        // Setando dimensões e posição da janela:
        this.setBounds(
            (desktopSolution.width - width) / 2,
            (desktopSolution.height - height) / 2,
            width,
            height
        );
    }

    //O método abaixo inicia o jogo, tornando a tela visível
    public void open() {
        stateManager.getScreen().startGame();
        this.setVisible(true);
    }

    public static void main(String arg[]) {
        GameFrame gameFrame = new GameFrame();
        gameFrame.open();
    }
}
