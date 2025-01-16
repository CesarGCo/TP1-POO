package com.gamestudio.elements;

import com.gamestudio.state.GameState;
import com.gamestudio.effect.Animation;
import com.gamestudio.manager.DataLoader;
import com.gamestudio.physical.PhysicalMap;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Rabbit extends DumbRobot {

    private Animation jumpingAnim;
    private int speedX;
    private boolean isFlipped;

    public Rabbit(int x, int y, GameState gameWorld) {
        super(x, y, 50, 30, 0, 50, gameWorld);
        jumpingAnim = DataLoader.getInstance().getAnimation("rabbit_jumping");
        speedX = 2;
        isFlipped = false;
        setDamage(3);
    }

    private void checkCollisionWithWall() {
        Rectangle currentBound = getBoundForCollisionWithMap();
        PhysicalMap physicalMap = getGameState().physicalMap;

        Rectangle rectLeftWall = physicalMap.haveCollisionWithLeftWall(currentBound);
        if (rectLeftWall != null) {
            setPosX(rectLeftWall.x + rectLeftWall.width + getWidth() / 2);
            speedX = Math.abs(speedX);
            jumpingAnim.flipAllImage();
            isFlipped = false;
        }

        Rectangle rectRightWall = physicalMap.haveCollisionWithRightWall(currentBound);
        if (rectRightWall != null) {
            setPosX(rectRightWall.x - getWidth() / 2);
            speedX = -Math.abs(speedX);
            jumpingAnim.flipAllImage();
            isFlipped = true;
        }
    }

    @Override
    public void move() {
        setPosX(getPosX() + speedX);
        checkCollisionWithWall();
    }

    @Override
    public void update() {
        super.update();
        move();
        jumpingAnim.Update(System.nanoTime());
    }

    @Override
    public Rectangle getBoundForCollisionWithEnemy() {
        return new Rectangle((int) (getPosX() - getWidth() / 2), (int) (getPosY() - getHeight() / 2), (int) getWidth(), (int) getHeight());
    }

    @Override
    public void draw(Graphics2D g2) {
        if (!isObjectOutOfCameraView()) {
            jumpingAnim.draw((int) (getPosX() - getGameState().camera.getPosX()),
                    (int) (getPosY() - getGameState().camera.getPosY()), g2);
        }
    }
}
