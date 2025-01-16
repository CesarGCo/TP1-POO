package com.gamestudio.elements;

import com.gamestudio.state.GameState;
import com.gamestudio.effect.Animation;
import com.gamestudio.manager.DataLoader;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Bat extends DumbRobot {

    private Animation flyingAnim;
    private float speed;

    public Bat(int x, int y, GameState gameWorld) {
        super(x, y, 50, 30, 0, 50, gameWorld);
        flyingAnim = DataLoader.getInstance().getAnimation("bat");
        speed = 2;
        setDamage(5);
    }

    @Override
    public void move() {
        float deltaX = getGameState().megaMan.getPosX() - getPosX();
        float deltaY = getGameState().megaMan.getPosY() - getPosY();
        float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        if (distance > 0) {
            setSpeedX((int) (deltaX / distance * speed));
            setSpeedY((int) (deltaY / distance * speed));
        }

        setPosX(getPosX() + getSpeedX());
        setPosY(getPosY() + getSpeedY());
    }

    @Override
    public void update() {
        super.update();

    }

    @Override
    public Rectangle getBoundForCollisionWithEnemy() {
        return new Rectangle((int) (getPosX() - getWidth() / 2), (int) (getPosY() - getHeight() / 2), (int) getWidth(), (int) getHeight());
    }

    @Override
    public void draw(Graphics2D g2) {
        if (!isObjectOutOfCameraView()) {
            flyingAnim.Update(System.nanoTime());
            flyingAnim.draw((int) (getPosX() - getGameState().camera.getPosX()),
                    (int) (getPosY() - getGameState().camera.getPosY()), g2);
        }
    }
}