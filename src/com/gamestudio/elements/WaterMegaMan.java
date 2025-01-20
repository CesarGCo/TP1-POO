package com.gamestudio.elements;

import com.gamestudio.manager.DataLoader;
import com.gamestudio.state.GameState;
import com.gamestudio.effect.Animation;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class WaterMegaMan extends MegaMan {

    private final Animation waterRunForwardAnim, waterRunBackAnim, waterRunShootingForwardAnim, waterRunShootingBackAnim;
    private final Animation waterIdleForwardAnim, waterIdleBackAnim, waterIdleShootingForwardAnim, waterIdleShootingBackAnim;
    private final Animation waterFlyForwardAnim, waterFlyBackAnim, waterFlyShootingForwardAnim, waterFlyShootingBackAnim;
    private final Animation waterBehurtForwardAnim, waterBehurtBackAnim;
    private boolean isShooting = false;
    private final Image face;
    private final ArrayList<Image> lifeBar;
    private final Clip hurtingSound;
    private final Clip shooting1;
    private long lastShootingTime;
    private final Timer lifeBarTimer;

    public WaterMegaMan(int x, int y, GameState gameState) {
        super(x, y, gameState);
        this.lifeBar = new ArrayList<>();

        // Load water animations
        shooting1 = DataLoader.getInstance().getSound("Water_shoot");
        hurtingSound = DataLoader.getInstance().getSound("Mega_man_hit");
        waterRunForwardAnim = DataLoader.getInstance().getAnimation("water_mega_man_running");
        waterRunBackAnim = DataLoader.getInstance().getAnimation("water_mega_man_running");
        waterRunForwardAnim.flipAllImage();

        waterIdleForwardAnim = DataLoader.getInstance().getAnimation("water_mega_man_idle");
        waterIdleBackAnim = DataLoader.getInstance().getAnimation("water_mega_man_idle");
        waterIdleForwardAnim.flipAllImage();

        waterFlyForwardAnim = DataLoader.getInstance().getAnimation("water_mega_man_jump");
        waterFlyForwardAnim.setIsRepeated(false);
        waterFlyBackAnim = DataLoader.getInstance().getAnimation("water_mega_man_jump");
        waterFlyForwardAnim.setIsRepeated(false);
        waterFlyForwardAnim.flipAllImage();

        waterBehurtForwardAnim = DataLoader.getInstance().getAnimation("water_mega_man_hurt");
        waterBehurtBackAnim = DataLoader.getInstance().getAnimation("water_mega_man_hurt");
        waterBehurtForwardAnim.flipAllImage();

        waterIdleShootingForwardAnim = DataLoader.getInstance().getAnimation("water_mega_man_shooting");
        waterIdleShootingBackAnim = DataLoader.getInstance().getAnimation("water_mega_man_shooting");
        waterIdleShootingForwardAnim.flipAllImage();

        waterRunShootingForwardAnim = DataLoader.getInstance().getAnimation("water_mega_man_shooting_running");
        waterRunShootingBackAnim = DataLoader.getInstance().getAnimation("water_mega_man_shooting_running");
        waterRunShootingForwardAnim.flipAllImage();

        waterFlyShootingForwardAnim = DataLoader.getInstance().getAnimation("water_mega_man_shooting_jumping");
        waterFlyShootingBackAnim = DataLoader.getInstance().getAnimation("water_mega_man_shooting_jumping");
        waterFlyShootingForwardAnim.flipAllImage();

        setDeathAnimation(DataLoader.getInstance().getAnimation("mega_man_death"));

        for(int i = 1; i < 30; i++){
            lifeBar.addFirst(DataLoader.getInstance().getFrameImage("life_bar" + i).getImage());
        }

        this.face = DataLoader.getInstance().getFrameImage("water_mega_man_face").getImage();

        lifeBarTimer = new Timer(800, (ActionEvent e) -> {
            if(this.getAmountLife() < 29) {
                this.setAmountLife(this.getAmountLife() + 1); // Increase health
                lifeBar.addFirst(DataLoader.getInstance().getFrameImage("life_bar" + Math.max(this.getAmountLife() -1, 0)).getImage());
            }
        });

        lifeBarTimer.start();
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
                        waterFlyForwardAnim.Update(System.nanoTime());
                        if (isShooting) {
                            waterFlyShootingForwardAnim.setCurrentFrame(waterFlyForwardAnim.getCurrentFrame());
                            waterFlyShootingForwardAnim.draw((int) (getPosX() - getGameState().camera.getPosX()) + 10, (int) (getPosY() - getGameState().camera.getPosY()), g2d);
                        } else {
                            waterFlyForwardAnim.draw((int) (getPosX() - getGameState().camera.getPosX()), (int) (getPosY() - getGameState().camera.getPosY()), g2d);
                        }
                    } else {
                        waterFlyBackAnim.Update(System.nanoTime());
                        if (isShooting) {
                            waterFlyShootingBackAnim.setCurrentFrame(waterFlyBackAnim.getCurrentFrame());
                            waterFlyShootingBackAnim.draw((int) (getPosX() - getGameState().camera.getPosX()) - 10, (int) (getPosY() - getGameState().camera.getPosY()), g2d);
                        } else {
                            waterFlyBackAnim.draw((int) (getPosX() - getGameState().camera.getPosX()), (int) (getPosY() - getGameState().camera.getPosY()), g2d);
                        }
                    }
                } else {
                    if (getSpeedX() > 0) {
                        setWaterRunFowardAnimation(g2d, waterRunForwardAnim, waterRunShootingForwardAnim);
                    } else if (getSpeedX() < 0) {
                        setWaterRunFowardAnimation(g2d, waterRunBackAnim, waterRunShootingBackAnim);
                    } else {
                        if (getDirection() == RIGHT) {
                            setWaterIdleShootingAnimation(g2d, waterIdleShootingForwardAnim, waterIdleForwardAnim);
                        } else {
                            setWaterIdleShootingAnimation(g2d, waterIdleShootingBackAnim, waterIdleBackAnim);
                        }
                    }
                }
            } else {
                if (getDirection() == RIGHT) {
                    waterBehurtForwardAnim.Update(System.nanoTime());
                    waterBehurtForwardAnim.draw((int) (getPosX() - getGameState().camera.getPosX()), (int) (getPosY() - getGameState().camera.getPosY()), g2d);
                } else {
                    waterBehurtBackAnim.Update(System.nanoTime());
                    waterBehurtBackAnim.draw((int) (getPosX() - getGameState().camera.getPosX()), (int) (getPosY() - getGameState().camera.getPosY()), g2d);
                }
            }
        }
        drawWaterLifeBar(g2d);
    }

    public void drawWaterLifeBar(Graphics2D g2d) {
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


    void setWaterRunFowardAnimation(Graphics2D g2d, Animation waterrunForwardAnim, Animation waterrunShootingForwarAnim) {
        waterrunForwardAnim.Update(System.nanoTime());
        if (isShooting) {
            waterrunShootingForwarAnim.setCurrentFrame(waterrunForwardAnim.getCurrentFrame() - 1);
            waterrunShootingForwarAnim.draw((int)(getPosX() - getGameState().camera.getPosX()), (int)( getPosY() - getGameState().camera.getPosY()), g2d);
        } else
            waterrunForwardAnim.draw((int)(getPosX() - getGameState().camera.getPosX()), (int)( getPosY() - getGameState().camera.getPosY()), g2d);
        if (waterrunForwardAnim.getCurrentFrame() == 1) waterrunForwardAnim.setIgnoreFrame(0);
    }

    void setWaterIdleShootingAnimation(Graphics2D g2d, Animation wateridleShootingForwardAnim, Animation wateridleForwardAnim) {
        if (isShooting) {
            wateridleShootingForwardAnim.Update(System.nanoTime());
            wateridleShootingForwardAnim.draw((int)(getPosX() - getGameState().camera.getPosX()), (int)(getPosY() - getGameState().camera.getPosY()), g2d);
        } else {
            wateridleForwardAnim.Update(System.nanoTime());
            wateridleForwardAnim.draw((int)(getPosX() - getGameState().camera.getPosX()), (int)(getPosY() - getGameState().camera.getPosY()), g2d);
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
            Projectile projectile = new WaterMegaManBullet(getPosX(), getPosY(), getGameState());
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
