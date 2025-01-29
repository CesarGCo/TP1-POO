package com.gamestudio.elements;

import com.gamestudio.effect.Animation;
import com.gamestudio.manager.DataLoader;
import com.gamestudio.state.GameState;

import java.awt.Graphics2D;
import java.awt.Rectangle;

// Representa um projétil diaparado pelo Mega man
public class MegaManBullet extends Projectile {
    // Animações do projétil
    private final Animation forwardBulletAnim;
    private final Animation backBulletAnim;

    public MegaManBullet(float x, float y, GameState gameState) {
        super(x, y, 8, 8, 1.0f, 1, gameState);
        forwardBulletAnim = DataLoader.getInstance().getAnimation("plasma_bullet");
        backBulletAnim = DataLoader.getInstance().getAnimation("plasma_bullet");
        backBulletAnim.flipAllImage();
        setTeamType(GameEntity.ALLY_TEAM);
    }

    // Retorna parâmetros que serão utilizados para verificar a colisão com inimigos
    @Override
    public Rectangle getBoundForCollisionWithEnemy() {
        return getBoundForCollisionWithMap();
    }

    // Desenha o projétil na tela
    @Override
    public void draw(Graphics2D g2) {
        if (getSpeedX() > 0) {
            checkAndApplyIgnoredFrames(g2, forwardBulletAnim);
        } else {
            checkAndApplyIgnoredFrames(g2, backBulletAnim);
        }
    }

    // Verifica quais frames são ignorados e desenha o projétil
    private void checkAndApplyIgnoredFrames(Graphics2D g2, Animation bulletAnim) {
        if (!bulletAnim.isIgnoreFrame(0) && bulletAnim.getCurrentFrame() == 3) {
            bulletAnim.setIgnoreFrame(0);
            bulletAnim.setIgnoreFrame(1);
            bulletAnim.setIgnoreFrame(2);
        }

        forwardBulletAnim.Update(System.nanoTime());
        forwardBulletAnim.draw((int) (getPosX() - getGameState().camera.getPosX()), (int) (getPosY() - getGameState().camera.getPosY()), g2);
    }

    @Override
    public void update() {
        super.update();
    }
}