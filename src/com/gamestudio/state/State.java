package com.gamestudio.state;

import java.awt.image.BufferedImage;
import com.gamestudio.manager.StateManager;

// A classe State representa um estado no jogo
// No total existem 3 States, cada um deles com componentes diferentes e interações diferentes emtre eles
public abstract class State {
    private StateManager stateManager; // Acesso ao StateManager
    private BufferedImage bufferedImage; // Parâmetro utilizado para representar uma imagem em memória, permitindo manipulações rápidas e eficientes de pixels, como leitura, escrita e edição de imagens

    public State(StateManager stateManager, BufferedImage bufferedImage) {
       this.stateManager = stateManager;
       this.bufferedImage = bufferedImage;
    }
    
    public StateManager getStateManager() {
        return stateManager;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    // Atualiza todos os componetes, entidades, ou itens no estado
    public abstract void update();

    // Renderiza todos os elementos presentes no estado
    public abstract void render();
    
    // Realiza uma ação com base em qual tecla foi pressionada
    public abstract void setPressedButton(int code);
    
    // Realiza uma ação com base em qual tecla foi solta
    public abstract void setReleasedButton(int code);

}