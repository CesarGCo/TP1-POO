package com.gamestudio.elements;

import com.gamestudio.state.GameState;
import com.gamestudio.effect.Animation;
import com.gamestudio.manager.DataLoader;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Bat extends DumbRobot {

    private Animation idleAnim;
    private Animation openingWingsAnim;
    private Animation flyingAnim;
    private int speed;
    private boolean isActive;
    private boolean isWingsOpened;

    public Bat(int x, int y, GameState gameWorld) {
        super(x, y, 50, 30, 0, 50, gameWorld);
        idleAnim = DataLoader.getInstance().getAnimation("bat_idle");
        openingWingsAnim = DataLoader.getInstance().getAnimation("bat_opening_wings");
        flyingAnim = DataLoader.getInstance().getAnimation("bat_flying");
        speed = 2;
        setDamage(5);
        isActive = false;
        isWingsOpened = false;
    }

    private boolean isMegaManInRange() {
        float deltaX = getGameState().megaMan.getPosX() - getPosX();
        float deltaY = getGameState().megaMan.getPosY() - getPosY();
        float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        return distance <= 200; // Distância de ativação do morcego
    }

    private boolean hasOpeningWingsFinished() {
        return openingWingsAnim.isLastFrame();
    }

    @Override
    public void move() {
        if (isActive) {
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
    }

    @Override
    public void update() {
        super.update();

        if (isMegaManInRange()) {
            if (!isActive) {
                isActive = true;
                isWingsOpened = false;
                openingWingsAnim.reset();
            }
        } else {
            isActive = false;
        }

        if (isActive && !isWingsOpened && hasOpeningWingsFinished()) {
            isWingsOpened = true;
        }

        move();
    }

    @Override
    public Rectangle getBoundForCollisionWithEnemy() {
        return new Rectangle((int) (getPosX() - getWidth() / 2), (int) (getPosY() - getHeight() / 2), (int) getWidth(), (int) getHeight());
    }

    @Override
    public void draw(Graphics2D g2) {
        if (!isObjectOutOfCameraView()) {
            if (!isActive) {
                idleAnim.Update(System.nanoTime());
                idleAnim.draw((int) (getPosX() - getGameState().camera.getPosX()),
                        (int) (getPosY() - getGameState().camera.getPosY()), g2);
            } else if (!isWingsOpened) {
                openingWingsAnim.Update(System.nanoTime());
                openingWingsAnim.draw((int) (getPosX() - getGameState().camera.getPosX()),
                        (int) (getPosY() - getGameState().camera.getPosY()), g2);
            } else {
                flyingAnim.Update(System.nanoTime());
                flyingAnim.draw((int) (getPosX() - getGameState().camera.getPosX()),
                        (int) (getPosY() - getGameState().camera.getPosY()), g2);
            }
        }
    }
}
