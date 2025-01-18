package com.gamestudio.elements;

import com.gamestudio.effect.Animation;
import com.gamestudio.manager.DataLoader;
import com.gamestudio.state.GameState;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public class MegaManBullet extends Projectile {

    private final Animation forwardBulletAnim;
    private final Animation backBulletAnim;

    public MegaManBullet(float x, float y, GameState gameState) {
        super(x, y, 60, 30, 1, 10, gameState);
        forwardBulletAnim = DataLoader.getInstance().getAnimation("plasma_bullet");
        backBulletAnim = DataLoader.getInstance().getAnimation("plasma_bullet");
        backBulletAnim.flipAllImage();
    }

    @Override
    public Rectangle getBoundForCollisionWithEnemy() {
        return getBoundForCollisionWithMap();
    }

    @Override
    public void draw(Graphics2D g2) {
        if (getSpeedX() > 0) {
            checkAndApplyIgnoredFrames(g2, forwardBulletAnim);
        } else {
            checkAndApplyIgnoredFrames(g2, backBulletAnim);
        }
    }

    private void checkAndApplyIgnoredFrames(Graphics2D g2, Animation forwardBulletAnim) {
        if (!forwardBulletAnim.isIgnoreFrame(0) && forwardBulletAnim.getCurrentFrame() == 3) {
            forwardBulletAnim.setIgnoreFrame(0);
            forwardBulletAnim.setIgnoreFrame(1);
            forwardBulletAnim.setIgnoreFrame(2);
        }

        forwardBulletAnim.Update(System.nanoTime());
        forwardBulletAnim.draw((int) (getPosX() - getGameState().camera.getPosX()), (int) (getPosY() - getGameState().camera.getPosY()), g2);
    }

    @Override
    public void update() {
        if (forwardBulletAnim.isIgnoreFrame(0) || backBulletAnim.isIgnoreFrame(0))
            setPosX(getPosX() + getSpeedX());
        Robot object = getGameState().robotManager.getCollisionWidthEnemyObject(this);
        if (object != null && object.getCurrentState() == ALIVE) {
            object.setCurrentState(BEHURT);
            System.out.println("Bullet set behurt for enemy");
        }
    }

}