package com.gamestudio.elements;

import com.gamestudio.effect.Animation;
import com.gamestudio.manager.DataLoader;
import com.gamestudio.state.GameState;

import java.awt.*;

public class WoodMan extends SmartRobot {
    private static final int STATE_INTRO = 0;
    private static final int STATE_BEATING_CHEST = 1;
    private static final int STATE_IDLE = 2;
    private static final int STATE_JUMPING = 3;

    private int currentState;
    private long stateStartTime;

    private final Animation IntroAnimation;
    private final Animation IdleAnimation;
    private final Animation ChestBeatFowardAnimation, ChestBeatBackAnimation;
    private final Animation JumpingFowardAnimation, JumpingBackAnimation;
    private final Animation LeafShieldThrowFowardAnimation, LeafShieldThrowBackAnimation;


    public WoodMan(int x, int y, GameState gameState) {
        super(x, y, 32, 31, 0.1f, 2, gameState);
        currentState = STATE_INTRO;
        stateStartTime = System.currentTimeMillis();
        this.setDirection(WoodMan.RIGHT);
        setTeamType(ENEMY_TEAM);

        IntroAnimation = DataLoader.getInstance().getAnimation("wood_man_intro");

        ChestBeatFowardAnimation = DataLoader.getInstance().getAnimation("wood_man_beat");
        ChestBeatBackAnimation = DataLoader.getInstance().getAnimation("wood_man_beat");
        ChestBeatFowardAnimation.flipAllImage();

        JumpingFowardAnimation = DataLoader.getInstance().getAnimation("wood_man_jumping");
        JumpingBackAnimation = DataLoader.getInstance().getAnimation("wood_man_jumping");
        JumpingFowardAnimation.flipAllImage();

       LeafShieldThrowFowardAnimation = DataLoader.getInstance().getAnimation("wood_man_leaf_shield_throw");
        LeafShieldThrowBackAnimation = DataLoader.getInstance().getAnimation("wood_man_leaf_shield_throw");
        LeafShieldThrowFowardAnimation.flipAllImage();

        IdleAnimation = DataLoader.getInstance().getAnimation("wood_man_idle");
    }

    @Override
    public void run() {

    }

    @Override
    public void jump() {
        if (!getIsJumping()) {
            setIsJumping(true);
            setSpeedY(-3);
            setSpeedX(-3);
            JumpingFowardAnimation.reset();
            JumpingBackAnimation.reset();
        }
    }

    @Override
    public void stopRun() {

    }

    @Override
    public void attack() {

    }

    @Override
    public Rectangle getBoundForCollisionWithEnemy() {
        Rectangle rect = getBoundForCollisionWithMap();
        rect.x += (int) (getPosX() - (float) getWidth() / 2);
        rect.y += (int) (getPosY() - (float) getHeight() / 2);
        rect.width = getWidth();
        rect.height = getHeight();

        return rect;
    }

    @Override
    public void update() {
        super.update();

        long elapsedTime = System.currentTimeMillis() - stateStartTime;

        switch (currentState) {
            case STATE_INTRO:
                if (elapsedTime > 2000) {
                    currentState = STATE_BEATING_CHEST;
                    stateStartTime = System.currentTimeMillis();
                }
                break;

            case STATE_BEATING_CHEST:
                if (elapsedTime > 3000) {
                    currentState = STATE_IDLE;
                    stateStartTime = System.currentTimeMillis();
                }
                break;

            case STATE_IDLE:
                if (elapsedTime > 1000) {
                    currentState = STATE_JUMPING;
                    stateStartTime = System.currentTimeMillis();
                }
                break;

            case STATE_JUMPING:
                if (!getIsJumping()) {
                    currentState = STATE_BEATING_CHEST;
                    stateStartTime = System.currentTimeMillis();
                }
                break;
        }

        performCurrentStateAction();

        if (getDirection() == RIGHT && getPosX() <= 50) {
            setDirection(LEFT);
        } else if (getDirection() == LEFT && getPosX() >= getGameState().camera.getWidthView() - 50) { // Assume rightq boundary
            setDirection(RIGHT);
        }

        if(!getIsJumping()) setSpeedX(0);

    }

    private void performCurrentStateAction() {
        switch (currentState) {
            case STATE_INTRO:
                // Play intro animation
                IntroAnimation.Update(System.nanoTime());
                break;

            case STATE_BEATING_CHEST:
                if (getDirection() == RIGHT) {
                    ChestBeatBackAnimation.Update(System.nanoTime());
                } else {
                    ChestBeatFowardAnimation.Update(System.nanoTime());
                }
                break;

            case STATE_IDLE:
                break;

            case STATE_JUMPING:
                if (!getIsJumping()) {
                    jump();
                }
                if (getDirection() == RIGHT) {
                    setSpeedX(-1);
                } else {
                    setSpeedX(1);
                }
                break;
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        switch (currentState) {
            case STATE_INTRO:
                IntroAnimation.draw((int)(getPosX() - getGameState().camera.getPosX()), (int) (getPosY() - getGameState().camera.getPosY()), g2);
                if (getDirection() == RIGHT) {
                    ChestBeatBackAnimation.draw((int)(getPosX() - getGameState().camera.getPosX()), (int) (getPosY() - getGameState().camera.getPosY()), g2);
                } else {
                    ChestBeatFowardAnimation.draw((int)(getPosX() - getGameState().camera.getPosX()), (int) (getPosY() - getGameState().camera.getPosY()), g2);
                }
                break;

            case STATE_BEATING_CHEST:
                ChestBeatBackAnimation.draw((int)(getPosX() - getGameState().camera.getPosX()), (int) (getPosY() - getGameState().camera.getPosY()), g2);
                break;

            case STATE_IDLE:
                 IdleAnimation.draw((int)(getPosX() - getGameState().camera.getPosX()), (int) (getPosY() - getGameState().camera.getPosY()), g2);
                break;

            case STATE_JUMPING:
                if (getDirection() == RIGHT) {
                    JumpingBackAnimation.draw((int)(getPosX() - getGameState().camera.getPosX()), (int) (getPosY() - getGameState().camera.getPosY()), g2);
                } else {
                    JumpingFowardAnimation.draw((int)(getPosX() - getGameState().camera.getPosX()), (int) (getPosY() - getGameState().camera.getPosY()), g2);
                }
                break;
        }
    }
}
