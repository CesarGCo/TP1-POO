/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gamestudio.elements;

import java.awt.Graphics;

import com.gamestudio.state.GameState;

public class Camera extends GameElement {

    private int widthView;
    private int heightView;   

    private boolean isLocked = false;

    public Camera(int x, int y, int widthView, int heightView, GameState gameState) {
        super(x, y, gameState);
        this.widthView = widthView;
        this.heightView = heightView;
    }

    public void lock() {
        isLocked = true;
    }

    public void unlock() {
        isLocked = false;
    }

    @Override
    public void update() {

        // NOTE: WHEN SEE FINAL BOSS, THE CAMERA WON'T CHANGE THE POSITION,
        // AFTER THE TUTORIAL, CAMERA WILL SET THE NEW POS

        if (!isLocked) {

            MegaMan mainCharacter = getGameState().megaMan;

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