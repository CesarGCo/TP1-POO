package com.gamestudio.elements;

import com.gamestudio.effect.Animation;
import com.gamestudio.manager.DataLoader;
import com.gamestudio.state.GameState;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public class FireMegaManBullet extends Projectile {

    private final Animation forwardBulletAnim;
    private final Animation backBulletAnim;

    public FireMegaManBullet(float x, float y, GameState gameState) {
        super(x, y, 8, 8, 1.0f, 5, gameState);

        // Load fire-specific animations
        forwardBulletAnim = DataLoader.getInstance().getAnimation("fire_bullet");
        backBulletAnim = DataLoader.getInstance().getAnimation("fire_bullet");
        backBulletAnim.flipAllImage();

        setTeamType(Robot.ALLY_TEAM);
    }

    @Override
    public Rectangle getBoundForCollisionWithEnemy() {
        return getBoundForCollisionWithMap();
    }

    @Override
    public void draw(Graphics2D g2) {
        if (getSpeedX() > 0) {
            drawBulletAnimation(g2, forwardBulletAnim);
        } else {
            drawBulletAnimation(g2, backBulletAnim);
        }
    }

    private void drawBulletAnimation(Graphics2D g2, Animation bulletAnim) {
        bulletAnim.Update(System.nanoTime());
        bulletAnim.draw((int) (getPosX() - getGameState().camera.getPosX()),
                (int) (getPosY() - getGameState().camera.getPosY()), g2);
    }

    @Override
    public void update() {
        super.update();
    }
}
