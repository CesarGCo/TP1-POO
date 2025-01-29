package com.gamestudio.elements;

import com.gamestudio.effect.Animation;
import com.gamestudio.manager.DataLoader;
import com.gamestudio.state.GameState;

import java.awt.Graphics2D;
import java.awt.Rectangle;

// Classe que representa uma bala do poder de água do Mega Man
public class WaterMegaManBullet extends Projectile {
    // Animações do projétil
    private final Animation forwardBulletAnim;
    private final Animation backBulletAnim;

    public WaterMegaManBullet(float x, float y, GameState gameState) {
        super(x, y, 8, 8, 1.0f, 2, gameState);

        forwardBulletAnim = DataLoader.getInstance().getAnimation("water_bullet");
        backBulletAnim = DataLoader.getInstance().getAnimation("water_bullet");
        backBulletAnim.flipAllImage();

        setTeamType(GameEntity.ALLY_TEAM);
    }

    // Retorna parâmetros que serão utilizados para verificar a colisão com inimigos
    @Override
    public Rectangle getBoundForCollisionWithEnemy() {
        return getBoundForCollisionWithMap();
    }

    // Desenha projétil nana tela
    @Override
    public void draw(Graphics2D g2) {
        if (getSpeedX() > 0) {
            drawBulletAnimation(g2, forwardBulletAnim);
        } else {
            drawBulletAnimation(g2, backBulletAnim);
        }
    }

    // Desenha a animação do projétil
    private void drawBulletAnimation(Graphics2D g2, Animation bulletAnim) {
        bulletAnim.Update(System.nanoTime());
        bulletAnim.draw((int) (getPosX() - getGameState().camera.getPosX()),
                (int) (getPosY() - getGameState().camera.getPosY()), g2);
    }

    // Atualiza o projétil
    @Override
    public void update() {
        super.update();
    }
}
