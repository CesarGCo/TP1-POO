package com.gamestudio.elements;

import com.gamestudio.manager.DataLoader;
import com.gamestudio.state.GameState;
import com.gamestudio.effect.Animation;

import javax.sound.sampled.Clip;
import javax.swing.Timer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

// A classe abaixo represena o poder de fogo do Mega man
// Logo,todas os métodos e atributos são idênticos à classe MegaMan
public class FireMegaMan extends MegaMan {

    private final Animation fireRunForwardAnim, fireRunBackAnim, fireRunShootingForwardAnim, fireRunShootingBackAnim;
    private final Animation fireIdleForwardAnim, fireIdleBackAnim, fireIdleShootingForwardAnim, fireIdleShootingBackAnim;
    private final Animation fireFlyForwardAnim, fireFlyBackAnim, fireFlyShootingForwardAnim, fireFlyShootingBackAnim;
    private final Animation fireBehurtForwardAnim, fireBehurtBackAnim;
    private boolean isShooting = false;
    private final Image face;
    private final ArrayList<Image> lifeBar;
    private final Clip hurtingSound;
    private final Clip shooting1;
    private long lastShootingTime;

    public FireMegaMan(int x, int y, GameState gameState) {
        super(x, y, gameState);
        this.lifeBar = new ArrayList<>();

        shooting1 = DataLoader.getInstance().getSound("Fire_shoot");
        hurtingSound = DataLoader.getInstance().getSound("Mega_man_hit");
        fireRunForwardAnim = DataLoader.getInstance().getAnimation("fire_mega_man_running");
        fireRunBackAnim = DataLoader.getInstance().getAnimation("fire_mega_man_running");
        fireRunForwardAnim.flipAllImage();

        fireIdleForwardAnim = DataLoader.getInstance().getAnimation("fire_mega_man_idle");
        fireIdleBackAnim = DataLoader.getInstance().getAnimation("fire_mega_man_idle");
        fireIdleForwardAnim.flipAllImage();

        fireFlyForwardAnim = DataLoader.getInstance().getAnimation("fire_mega_man_jump");
        fireFlyForwardAnim.setIsRepeated(false);
        fireFlyBackAnim = DataLoader.getInstance().getAnimation("fire_mega_man_jump");
        fireFlyForwardAnim.setIsRepeated(false);
        fireFlyForwardAnim.flipAllImage();

        fireBehurtForwardAnim = DataLoader.getInstance().getAnimation("fire_mega_man_hurt");
        fireBehurtBackAnim = DataLoader.getInstance().getAnimation("fire_mega_man_hurt");
        fireBehurtForwardAnim.flipAllImage();

        fireIdleShootingForwardAnim = DataLoader.getInstance().getAnimation("fire_mega_man_shooting");
        fireIdleShootingBackAnim = DataLoader.getInstance().getAnimation("fire_mega_man_shooting");
        fireIdleShootingForwardAnim.flipAllImage();

        fireRunShootingForwardAnim = DataLoader.getInstance().getAnimation("fire_mega_man_shooting_running");
        fireRunShootingBackAnim = DataLoader.getInstance().getAnimation("fire_mega_man_shooting_running");
        fireRunShootingForwardAnim.flipAllImage();

        fireFlyShootingForwardAnim = DataLoader.getInstance().getAnimation("fire_mega_man_shooting_jumping");
        fireFlyShootingBackAnim = DataLoader.getInstance().getAnimation("fire_mega_man_shooting_jumping");
        fireFlyShootingForwardAnim.flipAllImage();

        setDeathAnimation(DataLoader.getInstance().getAnimation("mega_man_death"));

        for(int i = 1; i < 30; i++){
            lifeBar.addFirst(DataLoader.getInstance().getFrameImage("life_bar" + i).getImage());
        }

        this.face = DataLoader.getInstance().getFrameImage("fire_mega_man_face").getImage();
    }

    @Override
    public void draw(Graphics2D g2d) {
        if (getIsExploding()) {
            drawDeathAnimation(g2d);
            return;
        }
        switch (getCurrentState()) {
            case ALIVE:
                if (!getIsInvencible()) {
                    if (getIsJumping()) {
                        if (getDirection() == RIGHT) {
                            fireFlyForwardAnim.Update(System.nanoTime());
                            if (isShooting) {
                                fireFlyShootingForwardAnim.setCurrentFrame(fireFlyForwardAnim.getCurrentFrame());
                                fireFlyShootingForwardAnim.draw((int) (getPosX() - getGameState().camera.getPosX()) + 10, (int) (getPosY() - getGameState().camera.getPosY()), g2d);
                            } else {
                                fireFlyForwardAnim.draw((int) (getPosX() - getGameState().camera.getPosX()), (int) (getPosY() - getGameState().camera.getPosY()), g2d);
                            }
                        } else {
                            fireFlyBackAnim.Update(System.nanoTime());
                            if (isShooting) {
                                fireFlyShootingBackAnim.setCurrentFrame(fireFlyBackAnim.getCurrentFrame());
                                fireFlyShootingBackAnim.draw((int) (getPosX() - getGameState().camera.getPosX()) - 10, (int) (getPosY() - getGameState().camera.getPosY()), g2d);
                            } else {
                                fireFlyBackAnim.draw((int) (getPosX() - getGameState().camera.getPosX()), (int) (getPosY() - getGameState().camera.getPosY()), g2d);
                            }
                        }
                    } else {
                        if (getSpeedX() > 0) {
                            setFireRunFowardAnimation(g2d, fireRunForwardAnim, fireRunShootingForwardAnim);
                        } else if (getSpeedX() < 0) {
                            setFireRunFowardAnimation(g2d, fireRunBackAnim, fireRunShootingBackAnim);
                        } else {
                            if (getDirection() == RIGHT) {
                                setFireIdleShootingAnimation(g2d, fireIdleShootingForwardAnim, fireIdleForwardAnim);
                            } else {
                                setFireIdleShootingAnimation(g2d, fireIdleShootingBackAnim, fireIdleBackAnim);
                            }
                        }
                    }
                } else {
                    if (getDirection() == RIGHT) {
                        fireBehurtForwardAnim.Update(System.nanoTime());
                        fireBehurtForwardAnim.draw((int) (getPosX() - getGameState().camera.getPosX()), (int) (getPosY() - getGameState().camera.getPosY()), g2d);
                    } else {
                        fireBehurtBackAnim.Update(System.nanoTime());
                        fireBehurtBackAnim.draw((int) (getPosX() - getGameState().camera.getPosX()), (int) (getPosY() - getGameState().camera.getPosY()), g2d);
                    }
                }
                break;
            case BEHURT:
                hurtingSound.setFramePosition(0); 
                hurtingSound.start();
                setIsInvencible(true);
                Timer timer = new Timer(500, (ActionEvent e) -> { 
                    setIsInvencible(false);
                    ((Timer) e.getSource()).stop();
                });
                timer.setRepeats(false);
                timer.start();
                break;
            default:
                break;
        }
        drawFireLifeBar(g2d);
    }

    public void drawFireLifeBar(Graphics2D g2d) {
        g2d.drawImage(face, 8, 10, null);
        g2d.drawImage(lifeBar.get(Math.max(getAmountLife()-1, 0)),10, 25, null);
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

    void setFireRunFowardAnimation(Graphics2D g2d, Animation firerunForwardAnim, Animation firerunShootingForwarAnim) {
        firerunForwardAnim.Update(System.nanoTime());
        if (isShooting) {
            firerunShootingForwarAnim.setCurrentFrame(firerunForwardAnim.getCurrentFrame() - 1);
            firerunShootingForwarAnim.draw((int)(getPosX() - getGameState().camera.getPosX()), (int)( getPosY() - getGameState().camera.getPosY()), g2d);
        } else
            firerunForwardAnim.draw((int)(getPosX() - getGameState().camera.getPosX()), (int)( getPosY() - getGameState().camera.getPosY()), g2d);
        if (firerunForwardAnim.getCurrentFrame() == 1) firerunForwardAnim.setIgnoreFrame(0);
    }

    void setFireIdleShootingAnimation(Graphics2D g2d, Animation fireidleShootingForwardAnim, Animation fireidleForwardAnim) {
        if (isShooting) {
            fireidleShootingForwardAnim.Update(System.nanoTime());
            fireidleShootingForwardAnim.draw((int)(getPosX() - getGameState().camera.getPosX()), (int)(getPosY() - getGameState().camera.getPosY()), g2d);
        } else {
            fireidleForwardAnim.Update(System.nanoTime());
            fireidleForwardAnim.draw((int)(getPosX() - getGameState().camera.getPosX()), (int)(getPosY() - getGameState().camera.getPosY()), g2d);
        }
    }
    @Override
    public void attack() {
        if (!isShooting) {
            if(shooting1.isRunning()) {
                shooting1.stop();
            } else {
                shooting1.setFramePosition(0);
                shooting1.start();
            }
            Projectile projectile = new FireMegaManBullet(getPosX(), getPosY(), getGameState());
            if (getDirection() == LEFT) {
                projectile.setSpeedX(-6);
                projectile.setPosX(projectile.getPosX() - 10);
                if (getSpeedX() != 0 && getSpeedY() == 0) {
                    projectile.setPosX(projectile.getPosX() - 6);
                    projectile.setPosY(projectile.getPosY());
                }
            } else {
                projectile.setSpeedX(6);
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
