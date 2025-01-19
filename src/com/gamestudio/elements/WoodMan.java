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
    private static final int STATE_LEAF_SHIELD_THROW = 4;

    private int currentState;
    private long stateStartTime;

    private final Animation IntroAnimation;
    private final Animation ChestBeatAnimation, ChestBeatBackAnimation;
    private final Animation IdleAnimation, IdleBackAnimation;
    private final Animation JumpingAnimation, JumpingBackAnimation;
    private final Animation LeafShieldThrowAnimation, LeafShieldThrowBackAnimation;

    private final Projectile leafShield = new LeafShield(getPosX(), getPosY(), getGameState());

    public WoodMan(int x, int y, GameState gameState) {
        super(x, y, 34, 31, 0.1f, 28, gameState);
        currentState = STATE_INTRO;
        stateStartTime = System.currentTimeMillis();
        this.setDirection(WoodMan.LEFT);
        setTeamType(ENEMY_TEAM);

        // Initialize animations
        IntroAnimation = DataLoader.getInstance().getAnimation("wood_man_intro");
        ChestBeatAnimation = DataLoader.getInstance().getAnimation("wood_man_beat");
        IdleAnimation = DataLoader.getInstance().getAnimation("wood_man_idle");
        JumpingAnimation = DataLoader.getInstance().getAnimation("wood_man_jumping");
        LeafShieldThrowAnimation = DataLoader.getInstance().getAnimation("wood_man_leaf_shield_throw");

        ChestBeatBackAnimation = DataLoader.getInstance().getAnimation("wood_man_beat");
        ChestBeatBackAnimation.flipAllImage();
        IdleBackAnimation = DataLoader.getInstance().getAnimation("wood_man_idle");
        IdleBackAnimation.flipAllImage();
        JumpingBackAnimation = DataLoader.getInstance().getAnimation("wood_man_jumping");
        JumpingBackAnimation.flipAllImage();
        LeafShieldThrowBackAnimation = DataLoader.getInstance().getAnimation("wood_man_leaf_shield_throw");
        LeafShieldThrowBackAnimation.flipAllImage();
    }

    @Override
    public void run() {
    }

    @Override
    public void jump() {
        if (!getIsJumping()) {
            setIsJumping(true);
            setSpeedY(-2);
            setSpeedX(getDirection() == LEFT ? -2 : 2);
        }
    }

    @Override
    public void stopRun() {
    }

    public void setUpAttack() {
        this.leafShield.setSpeedX(0);
        this.leafShield.setTeamType(this.getTeamType());
        getGameState().projectileManager.addObject(this.leafShield);
    }

    @Override
    public void attack() {
        this.leafShield.setSpeedX(this.getDirection() == LEFT ? -2 : 2);
        this.leafShield.setPosX(this.getPosX());
        this.leafShield.setPosY(this.getPosY());
    }

    @Override
    public Rectangle getBoundForCollisionWithEnemy() {
        Rectangle rect = getBoundForCollisionWithMap();
        rect.x = (int) (getPosX() - (float) getWidth() / 2);
        rect.y = (int) (getPosY() - (float) getHeight() / 2);
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
                    // Setup attack after chest beating
                    setUpAttack();
                    currentState = STATE_LEAF_SHIELD_THROW;
                    stateStartTime = System.currentTimeMillis();
                }
                break;

            case STATE_LEAF_SHIELD_THROW:
                if (elapsedTime > 1000) { // Adjust duration for throw animation
                    attack(); // Perform the attack (set projectile speed/direction)
                    currentState = STATE_IDLE;
                    stateStartTime = System.currentTimeMillis();
                }
                break;

            case STATE_IDLE:
                if (elapsedTime > 2000) { // Adjust idle duration
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

        // Stop horizontal movement if not jumping
        if (!getIsJumping()) setSpeedX(0);
    }


    private void performCurrentStateAction() {
        switch (currentState) {
            case STATE_INTRO:
                IntroAnimation.Update(System.nanoTime());
                break;

            case STATE_BEATING_CHEST:
                if (getDirection() == LEFT) {
                    ChestBeatAnimation.Update(System.nanoTime());
                } else {
                    ChestBeatBackAnimation.Update(System.nanoTime());
                }
                break;

            case STATE_LEAF_SHIELD_THROW:
                LeafShieldThrowAnimation.Update(System.nanoTime());
                break;

            case STATE_IDLE:
                if (getDirection() == LEFT) {
                    IdleAnimation.Update(System.nanoTime());
                } else {
                    IdleBackAnimation.Update(System.nanoTime());
                }
                break;

            case STATE_JUMPING:
                if (!getIsJumping()) jump();
                if (getDirection() == LEFT) {
                    JumpingAnimation.Update(System.nanoTime());
                } else {
                    JumpingBackAnimation.Update(System.nanoTime());
                }
                break;
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        int drawX = (int) (getPosX() - getGameState().camera.getPosX());
        int drawY = (int) (getPosY() - getGameState().camera.getPosY());

        Animation currentAnimation = getAnimation();

        if (getGameState().robotManager.getGameState().megaMan.getPosX() > this.getPosX()) {
            this.setDirection(WoodMan.RIGHT);
        } else {
            this.setDirection(WoodMan.LEFT);
        }

        currentAnimation.draw(drawX, drawY, g2);
    }

    private Animation getAnimation() {
        Animation currentAnimation;
        switch (currentState) {
            case STATE_INTRO -> currentAnimation = IntroAnimation;
            case STATE_BEATING_CHEST ->
                    currentAnimation = this.getDirection() == LEFT ? ChestBeatAnimation : ChestBeatBackAnimation;
            case STATE_IDLE -> currentAnimation = this.getDirection() == LEFT ? IdleAnimation : IdleBackAnimation;
            case STATE_JUMPING ->
                    currentAnimation = this.getDirection() == LEFT ? JumpingAnimation : JumpingBackAnimation;
            case STATE_LEAF_SHIELD_THROW ->
                    currentAnimation = this.getDirection() == LEFT ? LeafShieldThrowAnimation: LeafShieldThrowBackAnimation;
            default -> throw new IllegalStateException("Unexpected state: " + currentState);
        }
        return currentAnimation;
    }

}
