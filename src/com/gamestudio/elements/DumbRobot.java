package com.gamestudio.elements;

import com.gamestudio.state.GameState;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public abstract class DumbRobot extends Robot {

    public DumbRobot(int x, int y, float width, float height, float mass, int amountLife, GameState gameWorld) {
        super(x, y, width, height, mass, amountLife, gameWorld);
        setTeamType(ENEMY_TEAM);
    }

    public abstract void move();

    @Override
    public void update() {
        super.update();
        move();
    }

    @Override
    public abstract Rectangle getBoundForCollisionWithEnemy();

    @Override
    public abstract void draw(Graphics2D g2);
}
