package com.gamestudio.interfaces;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

import com.gamestudio.state.State;

// A classe abaixo é reponsável por organizar e agrupar os componentes gráficos
public class Screen extends JPanel implements Runnable, KeyListener{
    private State state; // Estado atual do jogo
    Thread gameThread; // Thread utilizado no GameLoop

    public Screen(State state) {
        this.state = state;
    }

    public void setState(State state) {
        this.state = state;
    }

    // O método abaixo cria e inicia uma nova thread para rodar o jogo separadamente da thread principal da interface gráfica.
    public void startGame(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    // Método responsável por exibir todos os componentes do estado atual na tela:
    @Override
    public void paint(Graphics g) {
        g.drawImage(state.getBufferedImage(), 0, 0, null);
    }

    // Método chamado caso alguma tecla seja pressionada:
    @Override
    public void keyPressed(KeyEvent e) {
        this.state.setPressedButton(e.getKeyCode());
    }

     // Método chamado caso alguma tecla seja solta:
    @Override
    public void keyReleased(KeyEvent e) {
        this.state.setReleasedButton(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    // O método abaixo implementa o Loop do Jogo
    // Para realizar o loop o método inicia a execução do jogo em uma thread separada
    @Override
    public void run() {

        long previousTime = System.nanoTime();
        long currentTime;
        long sleepTime;

        long period = 1000000000/80;

        while(true){

            
            state.update(); // Atualiza o estado atual do jogo
            state.render(); // Renderiza todos os elementos presentes no estado
            repaint(); // Exibi os elementos renderizados na tela

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
