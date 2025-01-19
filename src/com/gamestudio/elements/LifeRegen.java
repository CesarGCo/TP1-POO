package com.gamestudio.elements;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.gamestudio.manager.DataLoader;
import com.gamestudio.physical.PhysicalMap;
import com.gamestudio.state.GameState;

public class LifeRegen extends Item {
    private int life;
    public LifeRegen(float x, float y, GameState gameState) {
        super(x, y, 8, 8, gameState);
        this.life = 5;
        setItemaAnim(DataLoader.getInstance().getAnimation("life_regen"));
    }

    @Override
    public void update() {
        super.update();
        Robot object = getGameState().robotManager.getCollisionWidthItem(this);
        if(object != null) {
            object.regen(life);
            this.setCurrentState(DEATH);
        }
    }

    @Override
    public Rectangle getBoundForCollisionWithEnemy() {
        return new Rectangle((int) (getPosX() - getWidth() / 2), (int) (getPosY() - getHeight() / 2), (int) getWidth(), (int) getHeight());
    }

    @Override
    public void draw(Graphics2D g2) {
        getItemaAnim().Update(System.nanoTime());
        getItemaAnim().draw(
            (int)(getPosX() - getWidth() / 2 - getGameState().camera.getPosX()), 
            (int)(getPosY() - getHeight() / 2 - getGameState().camera.getPosY()), 
            g2
        );
    }
}
