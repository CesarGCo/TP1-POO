package com.gamestudio.elements;

import com.gamestudio.state.GameState;
import com.gamestudio.effect.Animation;
import com.gamestudio.manager.DataLoader;
import com.gamestudio.physical.PhysicalMap;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Rabbit extends DumbRobot {

    private static final int IDLE = 0;
    private static final int JUMPING = 1;

    private Animation jumpingAnim;
    private Animation idleAnim;
    private float speedX;
    private float speedY;
    private boolean isJumping;
    private float gravity;
    private long stateStartTime;// Tempo de espera entre os pulos (1 segundo)


    public Rabbit(float x, float y, GameState gameWorld) {
        super(x, y, 30, 30, 0.1f, 2, gameWorld);
        jumpingAnim = DataLoader.getInstance().getAnimation("robbit_jumping");
        idleAnim = DataLoader.getInstance().getAnimation("robbit_idle");
        setDeathAnimation(DataLoader.getInstance().getAnimation("explosion_effect"));
        speedX = -0.5f;
        speedY = 0.0f;
        gravity = 0.12f;
        isJumping = false;
        stateStartTime = System.currentTimeMillis();
        setCurrentAction(IDLE);
        setDamage(3);
    }

    private void checkCollisionWithGround() {
        Rectangle currentBound = getBoundForCollisionWithMap();
        PhysicalMap physicalMap = getGameState().physicalMap;

        Rectangle groundCollision = physicalMap.haveCollisionWithLandForDumbRobot(currentBound, this);
        if (groundCollision != null) {
            setPosY(groundCollision.y - getHeight() / 2.0f);
            speedY = 0;

            if (isJumping) {
                isJumping = false;
            }
        } else {
            speedY += gravity;
        }
    }

    private void checkCollisionWithWall() {
        Rectangle currentBound = getBoundForCollisionWithMap();
        PhysicalMap physicalMap = getGameState().physicalMap;

        Rectangle rectLeftWall = physicalMap.haveCollisionWithLeftWall(currentBound);
        if (rectLeftWall != null) {
            setPosX(rectLeftWall.x + rectLeftWall.width + getWidth() / 2);
            speedX = Math.abs(speedX);
            jumpingAnim.flipAllImage();
            idleAnim.flipAllImage();
        }

        Rectangle rectRightWall = physicalMap.haveCollisionWithRightWall(currentBound);
        if (rectRightWall != null) {
            setPosX(rectRightWall.x - getWidth() / 2);
            speedX = -Math.abs(speedX);
            jumpingAnim.flipAllImage();
            idleAnim.flipAllImage();
        }
    }

    private void jump() {
        if (!isJumping) {
            isJumping = true;
            speedY = -6.5f;
        }
    }


    @Override
    public void move() {
        if(getCurrentAction() == JUMPING) {
            setPosX(getPosX() + speedX);
        }
        setPosY(getPosY() + speedY);
        speedY += gravity;
        checkCollisionWithGround();
        checkCollisionWithWall();
    }

    @Override
    public void update() {
        super.update();
        System.out.println(getCurrentState());
        long elapsedTime = System.currentTimeMillis() - stateStartTime;

        switch (getCurrentState()) {
            case ALIVE:
                if(getCurrentAction() == IDLE) {
                    jumpingAnim.Update(System.nanoTime());
                    if (elapsedTime > 1000) {
                        setCurrentAction(JUMPING);
                        stateStartTime = System.currentTimeMillis();
                        jump();
                    }
                } else {
                    jumpingAnim.Update(System.nanoTime());
                    if (!isJumping) {
                        setCurrentAction(IDLE);
                        stateStartTime = System.currentTimeMillis();
                    }
                    break;

                }
            case DEATH:
                
                break;
        }
        move();
    }

    @Override
    public Rectangle getBoundForCollisionWithEnemy() {
        return new Rectangle((int) (getPosX() - getWidth() / 2),
                (int) (getPosY() - getHeight() / 2),
                (int) getWidth(), (int) getHeight());
    }

    @Override
    public void draw(Graphics2D g2) {
        if (!isObjectOutOfCameraView()) {
            Animation currentAnim = isJumping ? jumpingAnim : idleAnim;
            currentAnim.draw(
            (int) (getPosX() - getGameState().camera.getPosX()),
            (int) (getPosY() - getGameState().camera.getPosY()), g2);
            
        }
    }
}
