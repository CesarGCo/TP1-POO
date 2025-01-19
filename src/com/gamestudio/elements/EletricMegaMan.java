package com.gamestudio.elements;

import com.gamestudio.manager.DataLoader;
import com.gamestudio.state.GameState;
import com.gamestudio.effect.Animation;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.util.ArrayList;

public class EletricMegaMan extends MegaMan {

    private final Animation eletricRunForwardAnim, eletricRunBackAnim, eletricRunShootingForwardAnim, eletricRunShootingBackAnim;
    private final Animation eletricIdleForwardAnim, eletricIdleBackAnim, eletricIdleShootingForwardAnim, eletricIdleShootingBackAnim;
    private final Animation eletricFlyForwardAnim, eletricFlyBackAnim, eletricFlyShootingForwardAnim, eletricFlyShootingBackAnim;
    private final Animation eletricBehurtForwardAnim, eletricBehurtBackAnim;
    private boolean isShooting = false;
    private final Image face;
    private ArrayList<Image> lifeBar;
    private final Clip hurtingSound;
    private final Clip shooting1;
    private long lastShootingTime;

    public EletricMegaMan(int x, int y, GameState gameState) {
        super(x, y, gameState);
        this.lifeBar = new ArrayList<>();

        // Load eletric animations
        shooting1 = DataLoader.getInstance().getSound("Eletric_shoot");
        hurtingSound = DataLoader.getInstance().getSound("Mega_man_hit");
        eletricRunForwardAnim = DataLoader.getInstance().getAnimation("eletric_mega_man_running");
        eletricRunBackAnim = DataLoader.getInstance().getAnimation("eletric_mega_man_running");
        eletricRunForwardAnim.flipAllImage();

        eletricIdleForwardAnim = DataLoader.getInstance().getAnimation("eletric_mega_man_idle");
        eletricIdleBackAnim = DataLoader.getInstance().getAnimation("eletric_mega_man_idle");
        eletricIdleForwardAnim.flipAllImage();

        eletricFlyForwardAnim = DataLoader.getInstance().getAnimation("eletric_mega_man_jump");
        eletricFlyForwardAnim.setIsRepeated(false);
        eletricFlyBackAnim = DataLoader.getInstance().getAnimation("eletric_mega_man_jump");
        eletricFlyForwardAnim.setIsRepeated(false);
        eletricFlyForwardAnim.flipAllImage();

        eletricBehurtForwardAnim = DataLoader.getInstance().getAnimation("eletric_mega_man_hurt");
        eletricBehurtBackAnim = DataLoader.getInstance().getAnimation("eletric_mega_man_hurt");
        eletricBehurtForwardAnim.flipAllImage();

        eletricIdleShootingForwardAnim = DataLoader.getInstance().getAnimation("eletric_mega_man_shooting");
        eletricIdleShootingBackAnim = DataLoader.getInstance().getAnimation("eletric_mega_man_shooting");
        eletricIdleShootingForwardAnim.flipAllImage();

        eletricRunShootingForwardAnim = DataLoader.getInstance().getAnimation("eletric_mega_man_shooting_running");
        eletricRunShootingBackAnim = DataLoader.getInstance().getAnimation("eletric_mega_man_shooting_running");
        eletricRunShootingForwardAnim.flipAllImage();

        eletricFlyShootingForwardAnim = DataLoader.getInstance().getAnimation("eletric_mega_man_shooting_jumping");
        eletricFlyShootingBackAnim = DataLoader.getInstance().getAnimation("eletric_mega_man_shooting_jumping");
        eletricFlyShootingForwardAnim.flipAllImage();

        setDeathAnimation(DataLoader.getInstance().getAnimation("mega_man_death"));

        for (int i = 1; i < 30; i++) {
            lifeBar.addFirst(DataLoader.getInstance().getFrameImage("life_bar" + i).getImage());
        }

        this.face = DataLoader.getInstance().getFrameImage("eletric_mega_man_face").getImage();
    }

    @Override
    public void draw(Graphics2D g2d) {
        if (getIsExploding()) {
            drawDeathAnimation(g2d);
            return;
        }
        if (getCurrentState() == ALIVE) {
            if (!getIsInvencible()) {
                if (getIsJumping()) {
                    if (getDirection() == RIGHT) {
                        eletricFlyForwardAnim.Update(System.nanoTime());
                        if (isShooting) {
                            eletricFlyShootingForwardAnim.setCurrentFrame(eletricFlyForwardAnim.getCurrentFrame());
                            eletricFlyShootingForwardAnim.draw((int) (getPosX() - getGameState().camera.getPosX()) + 10, (int) (getPosY() - getGameState().camera.getPosY()), g2d);
                        } else {
                            eletricFlyForwardAnim.draw((int) (getPosX() - getGameState().camera.getPosX()), (int) (getPosY() - getGameState().camera.getPosY()), g2d);
                        }
                    } else {
                        eletricFlyBackAnim.Update(System.nanoTime());
                        if (isShooting) {
                            eletricFlyShootingBackAnim.setCurrentFrame(eletricFlyBackAnim.getCurrentFrame());
                            eletricFlyShootingBackAnim.draw((int) (getPosX() - getGameState().camera.getPosX()) - 10, (int) (getPosY() - getGameState().camera.getPosY()), g2d);
                        } else {
                            eletricFlyBackAnim.draw((int) (getPosX() - getGameState().camera.getPosX()), (int) (getPosY() - getGameState().camera.getPosY()), g2d);
                        }
                    }
                } else {
                    if (getSpeedX() > 0) {
                        setEletricRunFowardAnimation(g2d, eletricRunForwardAnim, eletricRunShootingForwardAnim);
                    } else if (getSpeedX() < 0) {
                        setEletricRunFowardAnimation(g2d, eletricRunBackAnim, eletricRunShootingBackAnim);
                    } else {
                        if (getDirection() == RIGHT) {
                            setEletricIdleShootingAnimation(g2d, eletricIdleShootingForwardAnim, eletricIdleForwardAnim);
                        } else {
                            setEletricIdleShootingAnimation(g2d, eletricIdleShootingBackAnim, eletricIdleBackAnim);
                        }
                    }
                }
            } else {
                if (getDirection() == RIGHT) {
                    eletricBehurtForwardAnim.Update(System.nanoTime());
                    eletricBehurtForwardAnim.draw((int) (getPosX() - getGameState().camera.getPosX()), (int) (getPosY() - getGameState().camera.getPosY()), g2d);
                } else {
                    eletricBehurtBackAnim.Update(System.nanoTime());
                    eletricBehurtBackAnim.draw((int) (getPosX() - getGameState().camera.getPosX()), (int) (getPosY() - getGameState().camera.getPosY()), g2d);
                }
            }
        }
        drawEletricLifeBar(g2d);
    }

    public void drawEletricLifeBar(Graphics2D g2d) {
        g2d.drawImage(face, 8, 10, null);
        g2d.drawImage(lifeBar.get(Math.max(getAmountLife() - 1, 0)), 10, 25, null);
    }

    @Override
    public void run() {
        if (getDirection() == LEFT)
            setSpeedX(-4);
        else setSpeedX(4);
    }

    @Override
    public void update() {
        super.update();
        if (isShooting) {
            if (System.nanoTime() - lastShootingTime > 900 * 200000) {

                isShooting = false;
            }
        }
    }

    void setEletricRunFowardAnimation(Graphics2D g2d, Animation eletricrunForwardAnim, Animation eletricrunShootingForwarAnim) {
        eletricrunForwardAnim.Update(System.nanoTime());
        if (isShooting) {
            eletricrunShootingForwarAnim.setCurrentFrame(eletricrunForwardAnim.getCurrentFrame() - 1);
            eletricrunShootingForwarAnim.draw((int) (getPosX() - getGameState().camera.getPosX()), (int) (getPosY() - getGameState().camera.getPosY()), g2d);
        } else
            eletricrunForwardAnim.draw((int) (getPosX() - getGameState().camera.getPosX()), (int) (getPosY() - getGameState().camera.getPosY()), g2d);
        if (eletricrunForwardAnim.getCurrentFrame() == 1) eletricrunForwardAnim.setIgnoreFrame(0);
    }

    void setEletricIdleShootingAnimation(Graphics2D g2d, Animation eletricidleShootingForwardAnim, Animation eletricidleForwardAnim) {
        if (isShooting) {
            eletricidleShootingForwardAnim.Update(System.nanoTime());
            eletricidleShootingForwardAnim.draw((int) (getPosX() - getGameState().camera.getPosX()), (int) (getPosY() - getGameState().camera.getPosY()), g2d);
        } else {
            eletricidleForwardAnim.Update(System.nanoTime());
            eletricidleForwardAnim.draw((int) (getPosX() - getGameState().camera.getPosX()), (int) (getPosY() - getGameState().camera.getPosY()), g2d);
        }
    }

    @Override
    public void attack() {
        if (!isShooting) {
            if (shooting1.isRunning()) {
                shooting1.stop();
            } else {
                shooting1.setFramePosition(0);
                shooting1.start();
            }
            Projectile projectile = new EletricMegaManBullet(getPosX(), getPosY(), getGameState());
            if (getDirection() == LEFT) {
                projectile.setSpeedX(-10);
                projectile.setPosX(projectile.getPosX() - 10);
                if (getSpeedX() != 0 && getSpeedY() == 0) {
                    projectile.setPosX(projectile.getPosX() - 6);
                    projectile.setPosY(projectile.getPosY());
                }
            } else {
                projectile.setSpeedX(10);
                projectile.setPosX(projectile.getPosX() + 8);
                if (getSpeedX() != 0 && getSpeedY() == 0) {
                    projectile.setPosX(projectile.getPosX() + 6);
                    projectile.setPosY(projectile.getPosY());
                }
            }
            if (getIsJumping())
                projectile.setPosY(projectile.getPosY() - 4);

            projectile.setTeamType(getTeamType());
            getGameState().projectileManager.addObject(projectile);

            this.lastShootingTime = System.nanoTime();
            this.isShooting = true;
        }
    }
}
