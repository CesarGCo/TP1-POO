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
    private final Animation ChestBeatFowardAnimation, ChestBeatBackAnimation;
    private final Animation JumpingFowardAnimation, JumpingBackAnimation;
    private final Animation LeafShieldThrowFowardAnimation, LeafShieldThrowBackAnimation;


    public WoodMan(int x, int y, int width, int height, float mass, int amountLife, GameState gameState) {
        super(x, y, width, height, mass, amountLife, gameState);
        currentState = STATE_INTRO;
        stateStartTime = System.currentTimeMillis();
        this.setDirection(WoodMan.LEFT);
        setTeamType(ENEMY_TEAM);

        IntroAnimation = DataLoader.getInstance().getAnimation("wood_man_into");

        ChestBeatFowardAnimation = DataLoader.getInstance().getAnimation("wood_man_beat");
        ChestBeatBackAnimation = DataLoader.getInstance().getAnimation("wood_man_beat");
        ChestBeatFowardAnimation.flipAllImage();

        JumpingFowardAnimation = DataLoader.getInstance().getAnimation("wood_man_jumping");
        JumpingBackAnimation = DataLoader.getInstance().getAnimation("wood_man_jumping");
        JumpingFowardAnimation.flipAllImage();

        LeafShieldThrowFowardAnimation = DataLoader.getInstance().getAnimation("wood_man_leaf_shield_throw");
        LeafShieldThrowBackAnimation = DataLoader.getInstance().getAnimation("wood_man_leaf_shield_throw");
        LeafShieldThrowFowardAnimation.flipAllImage();
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
        rect.x += getWidth() / 2;
        rect.y += getHeight() / 2;
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

        if (getDirection() == LEFT && getPosX() <= 50) {
            setDirection(RIGHT);
        } else if (getDirection() == RIGHT && getPosX() >= getGameState().camera.getWidthView() - 50) { // Assume right boundary
            setDirection(LEFT);
        }

    }

    private void performCurrentStateAction() {
        switch (currentState) {
            case STATE_INTRO:
                // Play intro animation
                IntroAnimation.Update(System.currentTimeMillis());
                break;

            case STATE_BEATING_CHEST:
                if (getDirection() == LEFT) {
                    ChestBeatBackAnimation.Update(System.currentTimeMillis());
                } else {
                    ChestBeatFowardAnimation.Update(System.currentTimeMillis());
                }
                break;

            case STATE_IDLE:
                break;

            case STATE_JUMPING:
                if (!getIsJumping()) {
                    jump();
                }
                if (getDirection() == LEFT) {
                    setSpeedX(-3);
                } else {
                    setSpeedX(3);
                }
                break;
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        switch (currentState) {
            case STATE_INTRO:
                IntroAnimation.draw((int) getPosX(), (int) getPosY(), g2);
                break;

            case STATE_BEATING_CHEST:
                if (getDirection() == LEFT) {
                    ChestBeatBackAnimation.draw((int) getPosX(), (int) getPosY(), g2);
                } else {
                    ChestBeatFowardAnimation.draw((int) getPosX(), (int) getPosY(), g2);
                }
                break;

            case STATE_IDLE:
                break;

            case STATE_JUMPING:
                if (getDirection() == LEFT) {
                    JumpingBackAnimation.draw((int) getPosX(), (int) getPosY(), g2);
                } else {
                    JumpingFowardAnimation.draw((int) getPosX(), (int) getPosY(), g2);
                }
                break;
        }
    }
}
