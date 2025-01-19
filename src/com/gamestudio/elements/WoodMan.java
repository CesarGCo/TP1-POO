package com.gamestudio.elements;

import com.gamestudio.effect.Animation;
import com.gamestudio.manager.DataLoader;
import com.gamestudio.state.GameState;

import java.awt.*;

public class WoodMan extends SmartRobot {
    private static final int INTRO = 0;
    private static final int BEATING_CHEST = 1;
    private static final int IDLE = 2;
    private static final int JUMPING = 3;
    private static final int LEAF_SHIELD_THROW = 4;

    private long stateStartTime;

    private final Animation IntroAnimation;
    private final Animation ChestBeatAnimation, ChestBeatBackAnimation;
    private final Animation IdleAnimation, IdleBackAnimation;
    private final Animation JumpingAnimation, JumpingBackAnimation;
    private final Animation LeafShieldThrowAnimation, LeafShieldThrowBackAnimation;

    private final Projectile leafShield = new LeafShield(getPosX(), getPosY(), getGameState());

    public WoodMan(int x, int y, GameState gameState) {
        super(x, y, 34, 31, 0.1f, 32, gameState);
        setCurrentAction(INTRO);
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
        System.out.println("Wood-Man setup up attack");
        this.leafShield.setSpeedX(0);
        this.leafShield.setPosY(this.getPosY());
        this.leafShield.setPosX(this.getPosX());
        this.leafShield.setTeamType(this.getTeamType());
        getGameState().projectileManager.addObject(this.leafShield);
    }

    @Override
    public void attack() {
        System.out.println("Wood-Man attack!");
        this.leafShield.setSpeedX(this.getDirection() == LEFT ? -1 : 1);
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

        switch (getCurrentAction()) {
            case INTRO:
                if (elapsedTime > 2000) {
                    setCurrentAction(BEATING_CHEST);
                    stateStartTime = System.currentTimeMillis();
                }
                break;

            case BEATING_CHEST:
                if (elapsedTime > 3000) {
                    // Setup attack after chest beating
                    setUpAttack();
                    setCurrentAction(LEAF_SHIELD_THROW);
                    stateStartTime = System.currentTimeMillis();
                }
                break;

            case LEAF_SHIELD_THROW:
                if (elapsedTime > 1000) { // Adjust duration for throw animation
                    attack(); // Perform the attack (set projectile speed/direction)
                    setCurrentAction(IDLE);
                    stateStartTime = System.currentTimeMillis();
                }
                break;

            case IDLE:
                if (elapsedTime > 2000) { // Adjust idle duration
                    setCurrentAction(JUMPING);
                    stateStartTime = System.currentTimeMillis();
                }
                break;

            case JUMPING:
                if (!getIsJumping()) {
                    setCurrentAction(BEATING_CHEST);
                    stateStartTime = System.currentTimeMillis();
                }
                break;
        }

        performCurrentStateAction();

        // Stop horizontal movement if not jumping
        if (!getIsJumping()) setSpeedX(0);
    }


    private void performCurrentStateAction() {
        switch (getCurrentAction()) {
            case INTRO:
                IntroAnimation.Update(System.nanoTime());
                break;

            case BEATING_CHEST:
                if (getDirection() == LEFT) {
                    ChestBeatAnimation.Update(System.nanoTime());
                } else {
                    ChestBeatBackAnimation.Update(System.nanoTime());
                }
                break;

            case LEAF_SHIELD_THROW:
                LeafShieldThrowAnimation.Update(System.nanoTime());
                break;

            case IDLE:
                if (getDirection() == LEFT) {
                    IdleAnimation.Update(System.nanoTime());
                } else {
                    IdleBackAnimation.Update(System.nanoTime());
                }
                break;

            case JUMPING:
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
        switch (getCurrentAction()) {
            case INTRO -> currentAnimation = IntroAnimation;
            case BEATING_CHEST ->
                    currentAnimation = this.getDirection() == LEFT ? ChestBeatAnimation : ChestBeatBackAnimation;
            case IDLE -> currentAnimation = this.getDirection() == LEFT ? IdleAnimation : IdleBackAnimation;
            case JUMPING ->
                    currentAnimation = this.getDirection() == LEFT ? JumpingAnimation : JumpingBackAnimation;
            case LEAF_SHIELD_THROW ->
                    currentAnimation = this.getDirection() == LEFT ? LeafShieldThrowAnimation: LeafShieldThrowBackAnimation;
            default -> throw new IllegalStateException("Unexpected state: " + getCurrentAction());
        }
        return currentAnimation;
    }

}
