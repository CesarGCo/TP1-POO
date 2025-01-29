package com.gamestudio.elements;

import com.gamestudio.effect.Animation;
import com.gamestudio.manager.DataLoader;
import com.gamestudio.state.GameState;

import java.awt.Graphics2D;
import java.awt.Rectangle;

// Classe que representa uma bala do poder de eletricidade do Mega Man
public class EletricMegaManBullet extends Projectile {

    private final Animation forwardBulletAnim;
    private final Animation backBulletAnim;

    public EletricMegaManBullet(float x, float y, GameState gameState) {
        super(x, y, 8, 8, 1.0f, 3, gameState);

        // Load eletric-specific animations
        forwardBulletAnim = DataLoader.getInstance().getAnimation("eletric_bullet");
        backBulletAnim = DataLoader.getInstance().getAnimation("eletric_bullet");
        backBulletAnim.flipAllImage();

        setTeamType(GameEntity.ALLY_TEAM);
    }

    @Override
    public Rectangle getBoundForCollisionWithEnemy() {
        return getBoundForCollisionWithMap();
    }

    // Desenha a bala na tela
    @Override
    public void draw(Graphics2D g2) {
        if (getSpeedX() > 0) {
            drawBulletAnimation(g2, forwardBulletAnim);
        } else {
            drawBulletAnimation(g2, backBulletAnim);
        }
    }

    // Atualiza a animação e à desenha
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
