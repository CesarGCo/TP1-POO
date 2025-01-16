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
    private final Animation landingForwardAnim, landingBackAnim;
    private final Animation behurtForwardAnim, behurtBackAnim;

    private long lastShootingTime;
    private boolean isShooting = false;

    private final Clip hurtingSound;
    private final Clip shooting1;

    public MegaMan(int x, int y, GameState gameState) {
        super(x, y, 70, 90, 0.1f, 100, gameState);

        shooting1 = DataLoader.getInstance().getMusic("bluefireshooting");
        hurtingSound = DataLoader.getInstance().getMusic("megamanhurt");

        setTeamType(ALLY_TEAM);

        runForwardAnim = DataLoader.getInstance().getAnimation("run");
        runBackAnim = DataLoader.getInstance().getAnimation("run");
        runBackAnim.flipAllImage();

        idleForwardAnim = DataLoader.getInstance().getAnimation("idle");
        idleBackAnim = DataLoader.getInstance().getAnimation("idle");
        idleBackAnim.flipAllImage();

        flyForwardAnim = DataLoader.getInstance().getAnimation("flyingup");
        flyForwardAnim.setIsRepeated(false);
        flyBackAnim = DataLoader.getInstance().getAnimation("flyingup");
        flyBackAnim.setIsRepeated(false);
        flyBackAnim.flipAllImage();

        landingForwardAnim = DataLoader.getInstance().getAnimation("landing");
        landingBackAnim = DataLoader.getInstance().getAnimation("landing");
        landingBackAnim.flipAllImage();

        behurtForwardAnim = DataLoader.getInstance().getAnimation("hurting");
        behurtBackAnim = DataLoader.getInstance().getAnimation("hurting");
        behurtBackAnim.flipAllImage();

        idleShootingForwardAnim = DataLoader.getInstance().getAnimation("idleshoot");
        idleShootingBackAnim = DataLoader.getInstance().getAnimation("idleshoot");
        idleShootingBackAnim.flipAllImage();

        runShootingForwarAnim = DataLoader.getInstance().getAnimation("runshoot");
        runShootingBackAnim = DataLoader.getInstance().getAnimation("runshoot");
        runShootingBackAnim.flipAllImage();

        flyShootingForwardAnim = DataLoader.getInstance().getAnimation("flyingupshoot");
        flyShootingBackAnim = DataLoader.getInstance().getAnimation("flyingupshoot");
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

        if (getIsLanding()) {
            landingBackAnim.Update(System.nanoTime());
            if (landingBackAnim.isLastFrame()) {
                setIsLanding(false);
                landingBackAnim.reset();
                runForwardAnim.reset();
                runBackAnim.reset();
            }
        }

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
                if (getIsLanding()) {
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

                } else if (getIsJumping()) {

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
       /* if (!isShooting) {
            shooting1.play();

            Bullet bullet = new BlueFire(getPosX(), getPosY(), getGameState());
            if (getDirection() == LEFT) {
                bullet.setSpeedX(-10);
                bullet.setPosX(bullet.getPosX() - 40);
                if (getSpeedX() != 0 && getSpeedY() == 0) {
                    bullet.setPosX(bullet.getPosX() - 10);
                    bullet.setPosY(bullet.getPosY() - 5);
                }
            } else {
                bullet.setSpeedX(10);
                bullet.setPosX(bullet.getPosX() + 40);
                if (getSpeedX() != 0 && getSpeedY() == 0) {
                    bullet.setPosX(bullet.getPosX() + 10);
                    bullet.setPosY(bullet.getPosY() - 5);
                }
            }
            if (getIsJumping())
                bullet.setPosY(bullet.getPosY() - 20);

            bullet.setTeamType(getTeamType());
            getGameState().bulletManager.addObject(bullet);

            lastShootingTime = System.nanoTime();
            isShooting = true;

        }*/

    }

    @Override
    public void hurtingCallback() {
        System.out.println("Call back hurting");
       // hurtingSound.play();
    }

}