package com.gamestudio.elements;

import com.gamestudio.state.GameState;

import java.awt.*;

public class MegaMan extends SmartRobot {

    public MegaMan(int x, int y, float width, float height, float mass, int amountLife, GameState gameState) {
        super(x, y, width, height, mass, amountLife, gameState);
    }

    @Override
    public void run() {

    }

    @Override
    public void jump() {

    }

    @Override
    public void stopRun() {

    }

    @Override
    public void attack() {

    }

    @Override
    public Rectangle getBoundForCollisionWithEnemy() {
        return null;
    }

    @Override
    public void draw(Graphics2D var1) {

    }

    @Override
    public void update() {

    }
}
