package com.gamestudio.elements;

import com.gamestudio.state.GameState;
import java.awt.Graphics2D;

/**
 * TODO separar gameEntity em gameEntity e GameObject. GameEntity será superclass de SmartGameEntity e DumbGameEntity,
 * enquanto projectile herda de GameObject (ou outro nome mais descritivo)
 */
public abstract class Projectile extends GameEntity {
    public static final int COLLIDED = 4;

    public Projectile(float x, float y, int width, int height, float mass, int damage, GameState gameState) {
        super(x, y, width, height, mass, 1, gameState);
        setDamage(damage);
    }

    public abstract void draw(Graphics2D g2d);

    public void update(){
        super.update();
        setPosX(getPosX() + getSpeedX());
        setPosY(getPosY() + getSpeedY());
        GameEntity object = getGameState().gameEntityManager.getCollisionWidthEnemyObject(this);
        if(object!=null && !object.getIsInvencible()) {
            object.beHurt(getDamage());
            this.setCurrentState(COLLIDED);
        }
    }

}