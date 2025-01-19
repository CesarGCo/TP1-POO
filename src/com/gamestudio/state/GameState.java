package com.gamestudio.state;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.sound.sampled.Clip;

import com.gamestudio.elements.*;
import com.gamestudio.interfaces.GameFrame;
import com.gamestudio.manager.DataLoader;
import com.gamestudio.manager.ProjectileManager;
import com.gamestudio.manager.RobotManager;
import com.gamestudio.manager.StateManager;
import com.gamestudio.physical.PhysicalMap;

public class GameState extends State {
    public PhysicalMap physicalMap;
    public MegaMan megaMan;
    public RobotManager robotManager;
    public ProjectileManager projectileManager;
    public Camera camera;
    private BufferedImage mapImage;
    private boolean drawHiboxes = false;
    private final Clip levelMusic;
    private Clip bossMusic;
    private boolean bossFightStarted = false;

    private long transformationStartTime; // Tracks transformation start time
    private boolean isTransformed = false; // Indicates if transformed
    private boolean isOnCooldown = false; // Indicates if on cooldown
    private long cooldownStartTime; // Tracks cooldown start time

    public GameState(StateManager stateManager) {
        super(stateManager, new BufferedImage(GameFrame.width, GameFrame.height, BufferedImage.TYPE_INT_ARGB));
        this.levelMusic = DataLoader.getInstance().getSound("Level_soundtrack");
        initState();
    }

    public BufferedImage getMapImage() {
        return mapImage;
    }

    public void initState() {
        this.robotManager = new RobotManager(this);
        this.projectileManager = new ProjectileManager(this);

        this.physicalMap = new PhysicalMap(-16, 0, this);
        this.mapImage = DataLoader.getInstance().getFrameImage("new_map_fall").getImage();
        this.megaMan = new MegaMan(100, 100, this);
        this.camera = new Camera(0, 0, 400, 240, this);
        initEnemies();
        robotManager.addObject(megaMan);
        megaMan.setCurrentState(SmartRobot.ALIVE);
    }

    private void initEnemies() {
        Robot bat1 = new Bat(250, 80, this);
        bat1.setDirection(Robot.LEFT);
        bat1.setTeamType(Robot.ENEMY_TEAM);
        robotManager.addObject(bat1);

        Robot bat2 = new Bat(362, 80, this);
        bat2.setDirection(Robot.LEFT);
        bat2.setTeamType(Robot.ENEMY_TEAM);
        robotManager.addObject(bat2);

        Robot bat3 = new Bat(630, 60, this);
        bat3.setDirection(Robot.LEFT);
        bat3.setTeamType(Robot.ENEMY_TEAM);
        robotManager.addObject(bat3);

        Robot bat4 = new Bat(830, 60, this);
        bat4.setDirection(Robot.LEFT);
        bat4.setTeamType(Robot.ENEMY_TEAM);
        robotManager.addObject(bat4);

        Robot bat5 = new Bat(1000, 40, this);
        bat5.setDirection(Robot.LEFT);
        bat5.setTeamType(Robot.ENEMY_TEAM);
        robotManager.addObject(bat5);

        Robot bat6 = new Bat(1310, 20, this);
        bat6.setDirection(Robot.LEFT);
        bat6.setTeamType(Robot.ENEMY_TEAM);
        robotManager.addObject(bat6);

        Robot bat7 = new Bat(1462, 20, this);
        bat7.setDirection(Robot.LEFT);
        bat7.setTeamType(Robot.ENEMY_TEAM);
        robotManager.addObject(bat7);

        Robot bat8 = new Bat(1664, 12, this);
        bat8.setDirection(Robot.LEFT);
        bat8.setTeamType(Robot.ENEMY_TEAM);
        robotManager.addObject(bat8);

        Robot bat9 = new Bat(2062, 50, this);
        bat9.setDirection(Robot.LEFT);
        bat9.setTeamType(Robot.ENEMY_TEAM);
        robotManager.addObject(bat9);

        Robot bat10 = new Bat(2168, 30, this);
        bat10.setDirection(Robot.LEFT);
        bat10.setTeamType(Robot.ENEMY_TEAM);
        robotManager.addObject(bat10);

        Robot bat11 = new Bat(2308, 50, this);
        bat11.setDirection(Robot.LEFT);
        bat11.setTeamType(Robot.ENEMY_TEAM);
        robotManager.addObject(bat11);

        Robot robbit1 = new Rabbit(500, 100, this);
        robbit1.setDirection(Robot.RIGHT);
        robbit1.setTeamType(Robot.ENEMY_TEAM);
        robotManager.addObject(robbit1);

        Robot robbit2 = new Rabbit(930, 100, this);
        robbit2.setDirection(Robot.RIGHT);
        robbit2.setTeamType(Robot.ENEMY_TEAM);
        robotManager.addObject(robbit2);

        Robot robbit3 = new Rabbit(2030, 100, this);
        robbit3.setDirection(Robot.RIGHT);
        robbit3.setTeamType(Robot.ENEMY_TEAM);
        robotManager.addObject(robbit3);

        Robot robbit4 = new Rabbit(2492, 100, this);
        robbit4.setDirection(Robot.RIGHT);
        robbit4.setTeamType(Robot.ENEMY_TEAM);
        robotManager.addObject(robbit4);

        Robot goomba1 = new Goomba(384, 130, this);
        goomba1.setDirection(Robot.RIGHT);
        goomba1.setTeamType(Robot.ENEMY_TEAM);
        robotManager.addObject(goomba1);

        Robot goomba2 = new Goomba(2244, 100, this);
        goomba2.setDirection(Robot.RIGHT);
        goomba2.setTeamType(Robot.ENEMY_TEAM);
        robotManager.addObject(goomba2);
    }

    private void initBossBattle() {
        this.bossFightStarted = true;
        levelMusic.stop();
        bossMusic = DataLoader.getInstance().getSound("Boss_soundtrack");
        bossMusic.start();
        Robot woodman = new WoodMan(3000, 100, this);
        robotManager.addObject(woodman);
    }

    public void update() {
        camera.update();
        robotManager.updateObjects();
        projectileManager.updateObjects();

        if (isTransformed) {
            if (System.currentTimeMillis() - transformationStartTime >= 4000) {
                switchToNormalMegaMan();
                isTransformed = false;
                isOnCooldown = true;
                cooldownStartTime = System.currentTimeMillis();
            }
        }

        if (isOnCooldown) {
            if (System.currentTimeMillis() - cooldownStartTime >= 10000) {
                isOnCooldown = false;
            }
        }

        if (this.megaMan.getPosX() == 2850 && !this.bossFightStarted) {
            initBossBattle();
        }

        if (bossFightStarted && !bossMusic.isRunning()) {
            levelMusic.stop();
            bossMusic.setFramePosition(0);
            bossMusic.start();
        }

        if (!levelMusic.isRunning() && !this.bossFightStarted) {
            levelMusic.setFramePosition(0);
            levelMusic.start();
        }

        if (megaMan.getCurrentState() == SmartRobot.DEATH && !megaMan.getIsExploding()) {
            if(bossMusic.isRunning()) bossMusic.stop();
            if(levelMusic.isRunning()) levelMusic.stop();
            getStateManager().setCurrentState(StateManager.GAMEOVER);
            initState();
            bossFightStarted = false;
        }
    }

    public void render() {
        Graphics g = getBufferedImage().getGraphics();
        Graphics2D g2 = (Graphics2D) g;
        drawMap(g2);
        projectileManager.draw(g2);
        robotManager.draw(g2);
        if (drawHiboxes) {
            drawAllHitBox(g2);
        }
    }

    private void drawMap(Graphics2D g2d) {
        float scaleX = (float) GameFrame.width / camera.getWidthView();
        float scaleY = (float) GameFrame.height / camera.getHeightView();
        g2d.scale(scaleX, scaleY);

        g2d.drawImage(
                mapImage,
                (int) (-camera.getPosX()),
                (int) (-camera.getPosY()),
                null
        );
    }

    public void setPressedButton(int code) {
        switch (code) {
            case KeyEvent.VK_D:
                megaMan.setDirection(MegaMan.RIGHT);
                megaMan.run();
                break;

            case KeyEvent.VK_A:
                megaMan.setDirection(MegaMan.LEFT);
                megaMan.run();
                break;

            case KeyEvent.VK_SPACE:
                megaMan.jump();
                break;

            case KeyEvent.VK_H:
                megaMan.attack();
                break;

            case KeyEvent.VK_F1:
                drawHiboxes = !drawHiboxes;
                break;

            case KeyEvent.VK_F: // Transform to Fire Mega Man
                if (!isOnCooldown && !isTransformed) {
                    switchToFireMegaMan();
                    isTransformed = true;
                    transformationStartTime = System.currentTimeMillis();
                }
                break;
            case KeyEvent.VK_B: // Transform to Water Mega Man
                if (!isOnCooldown && !isTransformed) {
                    switchToWaterMegaMan();
                    isTransformed = true;
                    transformationStartTime = System.currentTimeMillis();
                }
                break;
        }
    }

    private void switchToFireMegaMan() {
        FireMegaMan fireMegaMan = new FireMegaMan(
                (int) megaMan.getPosX(),
                (int) megaMan.getPosY(),
                this
        );
        fireMegaMan.setAmountLife(megaMan.getAmountLife());
        fireMegaMan.setCurrentState(megaMan.getCurrentState());
        fireMegaMan.setDirection(megaMan.getDirection());
        robotManager.RemoveObject(megaMan);
        megaMan = fireMegaMan;
        robotManager.addObject(megaMan);
    }

    private void switchToWaterMegaMan() {
        WaterMegaMan waterMegaMan = new WaterMegaMan(
                (int) megaMan.getPosX(),
                (int) megaMan.getPosY(),
                this
        );
        waterMegaMan.setAmountLife(megaMan.getAmountLife());
        waterMegaMan.setCurrentState(megaMan.getCurrentState());
        waterMegaMan.setDirection(megaMan.getDirection());
        robotManager.RemoveObject(megaMan);
        megaMan = waterMegaMan;
        robotManager.addObject(megaMan);
    }

    private void switchToNormalMegaMan() {
        MegaMan normalMegaMan = new MegaMan(
                (int) megaMan.getPosX(),
                (int) megaMan.getPosY(),
                this
        );
        normalMegaMan.setAmountLife(megaMan.getAmountLife());
        normalMegaMan.setCurrentState(megaMan.getCurrentState());
        normalMegaMan.setDirection(megaMan.getDirection());
        robotManager.RemoveObject(megaMan);
        megaMan = normalMegaMan;
        robotManager.addObject(megaMan);
    }

    public void setReleasedButton(int code) {
        switch (code) {
            case KeyEvent.VK_D:
                if (megaMan.getSpeedX() > 0)
                    megaMan.stopRun();
                break;

            case KeyEvent.VK_A:
                if (megaMan.getSpeedX() < 0)
                    megaMan.stopRun();
                break;
        }
    }

    private void drawAllHitBox(Graphics2D g2d) {
        projectileManager.drawAllHitBox(g2d);
        robotManager.drawAllHitBox(g2d);
        physicalMap.draw(g2d);
    }
}
