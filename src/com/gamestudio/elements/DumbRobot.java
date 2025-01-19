package com.gamestudio.elements;

import com.gamestudio.manager.DataLoader;
import com.gamestudio.state.GameState;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.sound.sampled.Clip;

public abstract class DumbRobot extends Robot {
    public DumbRobot(float x, float y, int width, int height, float mass, int amountLife, GameState gameWorld) {
        super(x, y, width, height, mass, amountLife, gameWorld);
        setDeathSound(DataLoader.getInstance().getSound("enemy_death"));
        setDeathTime(400);
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
