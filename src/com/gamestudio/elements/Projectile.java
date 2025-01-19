package com.gamestudio.elements;

import com.gamestudio.state.GameState;
import java.awt.Graphics2D;

/**
 * TODO separar robot em robot e GameObject. Robot será superclass de SmartRobot e DumbRobot,
 * enquanto projectile herda de GameObject (ou outro nome mais descritivo)
 */
public abstract class Projectile extends Robot {

    public Projectile(float x, float y, int width, int height, float mass, int damage, GameState gameState) {
        super(x, y, width, height, mass, 1, gameState);
        setDamage(damage);
    }

    public abstract void draw(Graphics2D g2d);

    public void update(){
        super.update();
        setPosX(getPosX() + getSpeedX());
        setPosY(getPosY() + getSpeedY());
        Robot object = getGameState().robotManager.getCollisionWidthEnemyObject(this);
        if(object!=null && object.getCurrentState() == ALIVE) {
            object.beHurt(getDamage());
            this.setCurrentState(Robot.DEATH);
        }
    }

}