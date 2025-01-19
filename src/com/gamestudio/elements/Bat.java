package com.gamestudio.elements;

import com.gamestudio.state.GameState;
import com.gamestudio.effect.Animation;
import com.gamestudio.manager.DataLoader;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Desktop.Action;

public class Bat extends DumbRobot {
    private static final int ACTIVE = 0;
    private static final int ACTIVANTING = 2;
    private static final int NOACTIVE = 3;

    private Animation idleAnim;
    private Animation openingWingsAnim;
    private Animation flyingAnim;
    private float speed;

    public Bat(int x, int y, GameState gameWorld) {
        super(x, y, 20, 20, 0, 2, gameWorld);
        idleAnim = DataLoader.getInstance().getAnimation("batton_idle");
        openingWingsAnim = DataLoader.getInstance().getAnimation("batton_rising");
        flyingAnim = DataLoader.getInstance().getAnimation("batton_flying");
        speed = 0.3f;
        setDamage(1);
        setCurrentAction(NOACTIVE);
    }

    private boolean isMegaManInRange() {
        float deltaX = getGameState().megaMan.getPosX() - getPosX();
        float deltaY = getGameState().megaMan.getPosY() - getPosY();
        float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        return distance <= 100; // Distância de ativação do morcego
    }

    private boolean hasOpeningWingsFinished() {
        return openingWingsAnim.isLastFrame();
    }

    @Override
    public void move() {
        if (getCurrentAction() == ACTIVE) {
            float deltaX = getGameState().megaMan.getPosX() - getPosX();
            float deltaY = getGameState().megaMan.getPosY() - getPosY();
            float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

            if (distance > 0) {
                setSpeedX((float) (deltaX / distance * speed));
                setSpeedY((float) (deltaY / distance * speed));
            }

            setPosX(getPosX() + getSpeedX());
            setPosY(getPosY() + getSpeedY());
        }
    }

    @Override
public void update() {
    super.update();
    //System.out.println(getCurrentState());

    if (getCurrentAction() == NOACTIVE && isMegaManInRange()) {
        setCurrentAction(ACTIVANTING);
        openingWingsAnim.reset();
    }

    if (getCurrentAction() == ACTIVANTING && hasOpeningWingsFinished()) {
        setCurrentAction(ACTIVE);
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
            if (getCurrentAction() == NOACTIVE) {
                idleAnim.Update(System.nanoTime());
                idleAnim.draw((int) (getPosX() - getGameState().camera.getPosX()),
                        (int) (getPosY() - getGameState().camera.getPosY()), g2);
            } else if (getCurrentAction() == ACTIVANTING) {
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
