package com.gamestudio.elements;

import com.gamestudio.manager.DataLoader;
import com.gamestudio.state.GameState;
import com.gamestudio.effect.Animation;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.sound.sampled.*;

public class MegaMan extends SmartRobot {
    public static final int RUNSPEED = 3;
    private final Animation runForwardAnim, runBackAnim, runShootingForwarAnim, runShootingBackAnim;
    private final Animation idleForwardAnim, idleBackAnim, idleShootingForwardAnim, idleShootingBackAnim;
    private final Animation flyForwardAnim, flyBackAnim, flyShootingForwardAnim, flyShootingBackAnim;
    //private final Animation landingForwardAnim, landingBackAnim;
    private final Animation behurtForwardAnim, behurtBackAnim;

    private long lastShootingTime;
    private boolean isShooting = false;

    private final Clip hurtingSound;
    private final Clip shooting1;

    public MegaMan(int x, int y, GameState gameState) {
        super(x, y, 70, 90, 1, 100, gameState);

        shooting1 = DataLoader.getInstance().getMusic("Shoot");
        hurtingSound = DataLoader.getInstance().getMusic("Mega_man_hit");

        setTeamType(ALLY_TEAM);

        runForwardAnim = DataLoader.getInstance().getAnimation("megaman_running");
        runBackAnim = DataLoader.getInstance().getAnimation("megaman_running");
        runBackAnim.flipAllImage();

        idleForwardAnim = DataLoader.getInstance().getAnimation("megaman_idle");
        idleBackAnim = DataLoader.getInstance().getAnimation("megaman_idle");
        idleBackAnim.flipAllImage();

        flyForwardAnim = DataLoader.getInstance().getAnimation("megaman_jump");
        flyForwardAnim.setIsRepeated(false);
        flyBackAnim = DataLoader.getInstance().getAnimation("megaman_jump");
        flyBackAnim.setIsRepeated(false);
        flyBackAnim.flipAllImage();

        // landingForwardAnim = DataLoader.getInstance().getAnimation("landing");
        // landingBackAnim = DataLoader.getInstance().getAnimation("landing");
        // landingBackAnim.flipAllImage();

        behurtForwardAnim = DataLoader.getInstance().getAnimation("megaman_hurt");
        behurtBackAnim = DataLoader.getInstance().getAnimation("megaman_hurt");
        behurtBackAnim.flipAllImage();

        idleShootingForwardAnim = DataLoader.getInstance().getAnimation("megaman_shooting");
        idleShootingBackAnim = DataLoader.getInstance().getAnimation("megaman_shooting");
        idleShootingBackAnim.flipAllImage();

        runShootingForwarAnim = DataLoader.getInstance().getAnimation("megaman_shooting_running");
        runShootingBackAnim = DataLoader.getInstance().getAnimation("megaman_shooting_running");
        runShootingBackAnim.flipAllImage();

        flyShootingForwardAnim = DataLoader.getInstance().getAnimation("megaman_shooting_jumping");
        flyShootingBackAnim = DataLoader.getInstance().getAnimation("megaman_shooting_jumping");
        flyShootingBackAnim.flipAllImage();

    }

    @Override
    public void update() {
        super.update();

        if (isShooting) {
            if (System.nanoTime() - lastShootingTime > 500 * 1000000) {
                isShooting = false;
            }
        }

        // if (getIsLanding()) {
        //     landingBackAnim.Update(System.nanoTime());
        //     if (landingBackAnim.isLastFrame()) {
        //         setIsLanding(false);
        //         landingBackAnim.reset();
        //         runForwardAnim.reset();
        //         runBackAnim.reset();
        //     }
        // }

    }

    @Override
    public Rectangle getBoundForCollisionWithEnemy() {
        // TODO Auto-generated method stub
        Rectangle rect = getBoundForCollisionWithMap();
        rect.x =  getPosX() - 22;
        rect.y =  getPosY() - 40;
        rect.width = 44;
        rect.height = 80;

        return rect;
    }

    @Override
    public void draw(Graphics2D g2) {
        switch (getCurrentState()) {
            case ALIVE:
                /*if (getIsLanding()) {
                    if (getDirection() == RIGHT) {
                        landingForwardAnim.setCurrentFrame(landingBackAnim.getCurrentFrame());
                        landingForwardAnim.draw( (getPosX() - getGameState().camera.getPosX()),
                                 getPosY() -  getGameState().camera.getPosY() + (getBoundForCollisionWithMap().height / 2 - landingForwardAnim.getCurrentImage().getHeight() / 2),
                                g2);
                    } else {
                        landingBackAnim.draw( (getPosX() - getGameState().camera.getPosX()),
                                 getPosY() -  getGameState().camera.getPosY() + (getBoundForCollisionWithMap().height / 2 - landingBackAnim.getCurrentImage().getHeight() / 2),
                                g2);
                    }

                } else */if (getIsJumping()) {

                    if (getDirection() == RIGHT) {
                        flyForwardAnim.Update(System.nanoTime());
                        if (isShooting) {
                            flyShootingForwardAnim.setCurrentFrame(flyForwardAnim.getCurrentFrame());
                            flyShootingForwardAnim.draw( (getPosX() - getGameState().camera.getPosX()) + 10,  getPosY() -  getGameState().camera.getPosY(), g2);
                        } else
                            flyForwardAnim.draw( (getPosX() - getGameState().camera.getPosX()),  getPosY() -  getGameState().camera.getPosY(), g2);
                    } else {
                        flyBackAnim.Update(System.nanoTime());
                        if (isShooting) {
                            flyShootingBackAnim.setCurrentFrame(flyBackAnim.getCurrentFrame());
                            flyShootingBackAnim.draw( (getPosX() - getGameState().camera.getPosX()) - 10,  getPosY() -  getGameState().camera.getPosY(), g2);
                        } else
                            flyBackAnim.draw( (getPosX() - getGameState().camera.getPosX()),  getPosY() -  getGameState().camera.getPosY(), g2);
                    }

                } else {
                    if (getSpeedX() > 0) {
                        setRunFowardAnimation(g2, runForwardAnim, runShootingForwarAnim);
                    } else if (getSpeedX() < 0) {
                        setRunFowardAnimation(g2, runBackAnim, runShootingBackAnim);
                    } else {
                        if (getDirection() == RIGHT) {
                            setIdleShootingAnimation(g2, idleShootingForwardAnim, idleForwardAnim);
                        } else {
                            setIdleShootingAnimation(g2, idleShootingBackAnim, idleBackAnim);
                        }
                    }
                }
                break;

            case BEHURT:
                if (getDirection() == RIGHT) {
                    behurtForwardAnim.draw( (getPosX() - getGameState().camera.getPosX()),  getPosY() - getGameState().camera.getPosY(), g2);
                } else {
                    behurtBackAnim.setCurrentFrame(behurtForwardAnim.getCurrentFrame());
                    behurtBackAnim.draw((getPosX() - getGameState().camera.getPosX()), getPosY() - getGameState().camera.getPosY(), g2);
                }
                break;
            default:
                break;
        }
    }

    private void setIdleShootingAnimation(Graphics2D g2, Animation idleShootingForwardAnim, Animation idleForwardAnim) {
        if (isShooting) {
            idleShootingForwardAnim.Update(System.nanoTime());
            idleShootingForwardAnim.draw((getPosX() - getGameState().camera.getPosX()), getPosY() - getGameState().camera.getPosY(), g2);
        } else {
            idleForwardAnim.Update(System.nanoTime());
            idleForwardAnim.draw((getPosX() - getGameState().camera.getPosX()), getPosY() - getGameState().camera.getPosY(), g2);
        }
    }

    private void setRunFowardAnimation(Graphics2D g2, Animation runForwardAnim, Animation runShootingForwarAnim) {
        runForwardAnim.Update(System.nanoTime());
        if (isShooting) {
            runShootingForwarAnim.setCurrentFrame(runForwardAnim.getCurrentFrame() - 1);
            runShootingForwarAnim.draw((getPosX() - getGameState().camera.getPosX()),  getPosY() - getGameState().camera.getPosY(), g2);
        } else
            runForwardAnim.draw((getPosX() - getGameState().camera.getPosX()),  getPosY() - getGameState().camera.getPosY(), g2);
        if (runForwardAnim.getCurrentFrame() == 1) runForwardAnim.setIgnoreFrame(0);
    }

    @Override
    public void run() {
        if (getDirection() == LEFT)
            setSpeedX(-3);
        else setSpeedX(3);
    }

    @Override
    public void jump() {
        if (!getIsJumping()) {
            setIsJumping(true);
            setSpeedY(-5);
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
            //shooting1.play();
            Projectile projectile = new MegaManBullet(getPosX(), getPosY(), getGameState());
            if (getDirection() == LEFT) {
                projectile.setSpeedX(-10);
                projectile.setPosX(projectile.getPosX() - 40);
                if (getSpeedX() != 0 && getSpeedY() == 0) {
                    projectile.setPosX(projectile.getPosX() - 10);
                    projectile.setPosY(projectile.getPosY() - 5);
                }
            } else {
                projectile.setSpeedX(10);
                projectile.setPosX(projectile.getPosX() + 40);
                if (getSpeedX() != 0 && getSpeedY() == 0) {
                    projectile.setPosX(projectile.getPosX() + 10);
                    projectile.setPosY(projectile.getPosY() - 5);
                }
            }
            if (getIsJumping())
                projectile.setPosY(projectile.getPosY() - 20);

            projectile.setTeamType(getTeamType());
            getGameState().projectileManager.addObject(projectile);

            lastShootingTime = System.nanoTime();
            isShooting = true;

        }

    }

    @Override
    public void hurtingCallback() {
        System.out.println("Call back hurting");
       // hurtingSound.play();
    }

}