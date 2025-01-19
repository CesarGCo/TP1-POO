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
        this.megaMan.setAmountLife(20);
        this.megaMan.setCurrentState(Robot.ALIVE);
        this.camera = new Camera(0, 0, 400, 240, this);
        initEnemies();
        robotManager.addObject(megaMan);
        megaMan.setCurrentState(SmartRobot.ALIVE);
    }

    private void initEnemies() {
        Robot bat1 = new Bat(200, 102, this);
        bat1.setDirection(Robot.LEFT);
        bat1.setTeamType(Robot.ENEMY_TEAM);
        robotManager.addObject(bat1);

        Robot robbit1 = new Rabbit(150, 150, this);
        robotManager.addObject(robbit1);
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

        if (this.megaMan.getPosX() == 2850 && !this.bossFightStarted) {
            initBossBattle();
        }
        if(bossFightStarted && !bossMusic.isRunning()) {
            bossMusic.setFramePosition(0);
            bossMusic.start();
        }

        if(getStateManager().getCurrentState() == StateManager.GAMEOVER) {
            bossMusic.stop();
        }

        if (!levelMusic.isRunning() && !this.bossFightStarted) {
            levelMusic.setFramePosition(0);
            levelMusic.start();
        }

        if (megaMan.getCurrentState() == SmartRobot.DEATH && !megaMan.getIsExploding()) {
            levelMusic.stop();
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
        // Escala para ajustar o conteúdo da câmera à tela
        float scaleX = (float) GameFrame.width / camera.getWidthView();
        float scaleY = (float) GameFrame.height / camera.getHeightView();
        g2d.scale(scaleX, scaleY);

        g2d.drawImage(
                mapImage,
                (int) (-camera.getPosX()), // Ajuste horizontal proporcional à escala
                (int) (-camera.getPosY()), // Ajuste vertical proporcional à escala
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

            case KeyEvent.VK_F: // Switch to Fire Mega Man
                if (!(megaMan instanceof FireMegaMan)) {
                    switchToFireMegaMan();
                }
                break;

            case KeyEvent.VK_M: // Switch back to Normal Mega Man
                if (megaMan instanceof FireMegaMan) {
                    switchToNormalMegaMan();
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
        robotManager.RemoveObject(megaMan); // Remove old Mega Man
        megaMan = fireMegaMan;             // Replace with Fire Mega Man
        robotManager.addObject(megaMan);   // Add new Mega Man to RobotManager
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
        robotManager.RemoveObject(megaMan); // Remove old Mega Man
        megaMan = normalMegaMan;            // Replace with Normal Mega Man
        robotManager.addObject(megaMan);    // Add new Mega Man to RobotManager
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

            case KeyEvent.VK_SPACE:

                break;
        }
    }

    private void drawAllHitBox(Graphics2D g2d) {
        projectileManager.drawAllHitBox(g2d);
        robotManager.drawAllHitBox(g2d);
        physicalMap.draw(g2d);
    }
}
