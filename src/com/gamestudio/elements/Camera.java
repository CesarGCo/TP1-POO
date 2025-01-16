/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gamestudio.elements;

import com.gamestudio.state.GameState;

public class Camera extends GameElement {

    private float widthView;
    private float heightView;

    private boolean isLocked = false;

    public Camera(int x, int y, float widthView, float heightView, GameState gameState) {
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

            if (mainCharacter.getPosX() - getPosX() > 400) setPosX(mainCharacter.getPosX() - 400);
            if (mainCharacter.getPosX() - getPosX() < 200) setPosX(mainCharacter.getPosX() - 200);

            if (mainCharacter.getPosY() - getPosY() > 400) setPosY(mainCharacter.getPosY() - 400); // bottom
            else if (mainCharacter.getPosY() - getPosY() < 250) setPosY(mainCharacter.getPosY() - 250);// top
        }

    }

    public float getWidthView() {
        return widthView;
    }

    public void setWidthView(float widthView) {
        this.widthView = widthView;
    }

    public float getHeightView() {
        return heightView;
    }

    public void setHeightView(float heightView) {
        this.heightView = heightView;
    }
}