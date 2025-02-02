package com.gamestudio.elements;

import com.gamestudio.state.GameState;

public class Camera extends GameElement {
    //Dimensões da câmera:
    private int widthView;
    private int heightView;   

    public Camera(int x, int y, int widthView, int heightView, GameState gameState) {
        super(x, y, gameState);
        this.widthView = widthView;
        this.heightView = heightView;
    }

    // Atualiza a posição da câmera com base na posição do Mega Man
    @Override
    public void update() {
        MegaMan mainCharacter = getGameState().megaMan;
        if(mainCharacter.getPosX() > 100 && mainCharacter.getPosX() < 2830) {
            if(mainCharacter.getPosX() - getPosX() > 150) setPosX(mainCharacter.getPosX() - 150);
            if(mainCharacter.getPosX() - getPosX() < 100) setPosX(mainCharacter.getPosX() - 100);
        }
    }

    public int getWidthView() {
        return widthView;
    }

    public void setWidthView(int widthView) {
        this.widthView = widthView;
    }

    public int getHeightView() {
        return heightView;
    }

    public void setHeightView(int heightView) {
        this.heightView = heightView;
    }
}