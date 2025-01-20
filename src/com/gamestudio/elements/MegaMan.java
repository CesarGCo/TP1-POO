package com.gamestudio.elements;

import com.gamestudio.manager.DataLoader;
import com.gamestudio.state.GameState;
import com.gamestudio.effect.Animation;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.Timer;

import javax.sound.sampled.*;

public class MegaMan extends SmartGameEntity {
    private final Animation runForwardAnim, runBackAnim, runShootingForwarAnim, runShootingBackAnim;
    private final Animation idleForwardAnim, idleBackAnim, idleShootingForwardAnim, idleShootingBackAnim;
    private final Animation flyForwardAnim, flyBackAnim, flyShootingForwardAnim, flyShootingBackAnim;
    private final Animation behurtForwardAnim, behurtBackAnim;
    private final ArrayList<Image> lifeBar;
    private final Image face;
    private long lastShootingTime;
    private boolean isShooting = false;

    private final Clip hurtingSound;
    private final Clip shooting1;

    public MegaMan(int x, int y, GameState gameState) {
        super(x, y, 16, 24, 0.1f, 29, gameState);
        this.lifeBar = new ArrayList<>();
        shooting1 = DataLoader.getInstance().getSound("Shoot");
        hurtingSound = DataLoader.getInstance().getSound("Mega_man_hit");

        runForwardAnim = DataLoader.getInstance().getAnimation("mega_man_running");
        runBackAnim = DataLoader.getInstance().getAnimation("mega_man_running");
        runForwardAnim.flipAllImage();

        idleForwardAnim = DataLoader.getInstance().getAnimation("mega_man_idle");
        idleBackAnim = DataLoader.getInstance().getAnimation("mega_man_idle");
        idleForwardAnim.flipAllImage();

        flyForwardAnim = DataLoader.getInstance().getAnimation("mega_man_jump");
        flyForwardAnim.setIsRepeated(false);
        flyBackAnim = DataLoader.getInstance().getAnimation("mega_man_jump");
        flyForwardAnim.setIsRepeated(false);
        flyForwardAnim.flipAllImage();

        behurtForwardAnim = DataLoader.getInstance().getAnimation("mega_man_hurt");
        behurtBackAnim = DataLoader.getInstance().getAnimation("mega_man_hurt");
        behurtForwardAnim.flipAllImage();

        idleShootingForwardAnim = DataLoader.getInstance().getAnimation("mega_man_shooting");
        idleShootingBackAnim = DataLoader.getInstance().getAnimation("mega_man_shooting");
        idleShootingForwardAnim.flipAllImage();

        runShootingForwarAnim = DataLoader.getInstance().getAnimation("mega_man_shooting_running");
        runShootingBackAnim = DataLoader.getInstance().getAnimation("mega_man_shooting_running");
        runShootingForwarAnim.flipAllImage();

        flyShootingForwardAnim = DataLoader.getInstance().getAnimation("mega_man_shooting_jumping");
        flyShootingBackAnim = DataLoader.getInstance().getAnimation("mega_man_shooting_jumping");
        flyShootingForwardAnim.flipAllImage();
        setDeathAnimation(DataLoader.getInstance().getAnimation("mega_man_death"));


        for(int i = 1; i < 30; i++){
            lifeBar.addFirst(DataLoader.getInstance().getFrameImage("life_bar" + i).getImage());
        }
        
        this.face = DataLoader.getInstance().getFrameImage("mega_man_face").getImage();

        this.setDirection(MegaMan.RIGHT);
        setTeamType(ALLY_TEAM);
        setDeathSound(DataLoader.getInstance().getSound("death"));
        setDeathTime(800);
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

    @Override
    public Rectangle getBoundForCollisionWithEnemy() {
        // TODO Auto-generated method stub
        Rectangle rect = getBoundForCollisionWithMap();
        rect.x =  (int) (getPosX() - 8);
        rect.y =  (int) (getPosY() - 12);
        rect.width = 18;
        rect.height = 24;

        return rect;
    }

    @Override
    public void draw(Graphics2D g2d) {
        if(getIsExploding()){
            drawDeathAnimation(g2d);
            return;
        }
        switch (getCurrentState()) {
            case ALIVE:
                if(!getIsInvencible()) {
                    if (getIsJumping()) {
                        if (getDirection() == RIGHT) {
                            flyForwardAnim.Update(System.nanoTime());
                            if (isShooting) {
                                flyShootingForwardAnim.setCurrentFrame(flyForwardAnim.getCurrentFrame());
                                flyShootingForwardAnim.draw((int) (getPosX() - getGameState().camera.getPosX()) + 10, (int) (getPosY() -  getGameState().camera.getPosY()), g2d);
                            } else
                                flyForwardAnim.draw((int) (getPosX() - getGameState().camera.getPosX()), (int) (getPosY() -  getGameState().camera.getPosY()), g2d);
                        } else {
                            flyBackAnim.Update(System.nanoTime());
                            if (isShooting) {
                                flyShootingBackAnim.setCurrentFrame(flyBackAnim.getCurrentFrame());
                                flyShootingBackAnim.draw((int) (getPosX() - getGameState().camera.getPosX()) - 10,(int) (getPosY() -  getGameState().camera.getPosY()), g2d);
                            } else
                                flyBackAnim.draw((int) (getPosX() - getGameState().camera.getPosX()), (int) (getPosY() -  getGameState().camera.getPosY()), g2d);
                        }
    
                    } else {
                        if (getSpeedX() > 0) {
                            setRunFowardAnimation(g2d, runForwardAnim, runShootingForwarAnim);
                        } else if (getSpeedX() < 0) {
                            setRunFowardAnimation(g2d, runBackAnim, runShootingBackAnim);
                        } else {
                            if (getDirection() == RIGHT) {
                                setIdleShootingAnimation(g2d, idleShootingForwardAnim, idleForwardAnim);
                            } else {
                                setIdleShootingAnimation(g2d, idleShootingBackAnim, idleBackAnim);
                            }
                        }
                    }
                } else {
                    if (getDirection() == RIGHT) {
                        behurtForwardAnim.Update(System.nanoTime());
                        behurtForwardAnim.draw((int) (getPosX() - getGameState().camera.getPosX()),(int) (getPosY() - getGameState().camera.getPosY()), g2d);
                    } else {
                        behurtBackAnim.Update(System.nanoTime());
                        behurtBackAnim.draw((int)(getPosX() - getGameState().camera.getPosX()), (int) (getPosY() - getGameState().camera.getPosY()), g2d);
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
        drawLifeBar(g2d);
    }

    public void drawLifeBar(Graphics2D g2d) {
        g2d.drawImage(face, 8, 10, null);
        g2d.drawImage(lifeBar.get(Math.max(getAmountLife()-1, 0)),10, 25, null);
    }

    void setIdleShootingAnimation(Graphics2D g2d, Animation idleShootingForwardAnim, Animation idleForwardAnim) {
        if (isShooting) {
            idleShootingForwardAnim.Update(System.nanoTime());
            idleShootingForwardAnim.draw((int)(getPosX() - getGameState().camera.getPosX()), (int)(getPosY() - getGameState().camera.getPosY()), g2d);
        } else {
            idleForwardAnim.Update(System.nanoTime());
            idleForwardAnim.draw((int)(getPosX() - getGameState().camera.getPosX()), (int)(getPosY() - getGameState().camera.getPosY()), g2d);
        }
    }

    void setRunFowardAnimation(Graphics2D g2d, Animation runForwardAnim, Animation runShootingForwarAnim) {
        runForwardAnim.Update(System.nanoTime());
        if (isShooting) {
            runShootingForwarAnim.setCurrentFrame(runForwardAnim.getCurrentFrame() - 1);
            runShootingForwarAnim.draw((int)(getPosX() - getGameState().camera.getPosX()), (int)( getPosY() - getGameState().camera.getPosY()), g2d);
        } else
            runForwardAnim.draw((int)(getPosX() - getGameState().camera.getPosX()), (int)( getPosY() - getGameState().camera.getPosY()), g2d);
        if (runForwardAnim.getCurrentFrame() == 1) runForwardAnim.setIgnoreFrame(0);
    }

    @Override
    public void run() {
        if (getDirection() == LEFT)
            setSpeedX(-2);
        else setSpeedX(2);
    }

    @Override
    public void jump() {
        if (!getIsJumping()) {
            setIsJumping(true);
            setSpeedY(-3);
            flyBackAnim.reset();
            flyForwardAnim.reset();
        }
    }

    @Override
    public void stopRun() {
        setSpeedX(0);
        runForwardAnim.reset();
        runBackAnim.reset();
        runForwardAnim.unIgnoreFrame(0);
        runBackAnim.unIgnoreFrame(0);
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
            Projectile projectile = new MegaManBullet(getPosX(), getPosY(), getGameState());
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

            lastShootingTime = System.nanoTime();
            this.isShooting = true;
        }
    }

}