package com.gamestudio.state;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.Kernel;

import javax.sound.sampled.Clip;

import com.gamestudio.elements.Bat;
import com.gamestudio.elements.Camera;
import com.gamestudio.elements.Robot;
import com.gamestudio.elements.Bat;
import com.gamestudio.elements.MegaMan;
import com.gamestudio.elements.Rabbit;
import com.gamestudio.elements.SmartRobot;
import com.gamestudio.elements.WoodMan;
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
    private Clip levelMusic;

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
       this.megaMan = new MegaMan(100, 50, this);
       this.camera = new Camera(0, 0, 400, 240, this);
       robotManager.addObject(megaMan);
       megaMan.setCurrentState(SmartRobot.ALIVE);
       initEnemies();
    }

    private void initEnemies(){
        Robot bat1 = new Bat(200, 102, this);
        bat1.setDirection(Robot.LEFT);
        bat1.setTeamType(Robot.ENEMY_TEAM);
        robotManager.addObject(bat1);

        Robot robbit1 = new Rabbit(150, 150, this);
        robotManager.addObject(robbit1);

       Robot woodman = new WoodMan(3060, 100, this);
       robotManager.addObject(woodman);
    }

    public void update() {
        camera.update();
        projectileManager.updateObjects();
        robotManager.updateObjects();
        if(!levelMusic.isRunning()) {  
            levelMusic.setFramePosition(0); 
            levelMusic.start();
        }
        if(megaMan.getCurrentState() == SmartRobot.DEATH) {
            levelMusic.stop();
            getStateManager().setCurrentState(StateManager.GAMEOVER);
            initState();
        }
    }

    public void render() {
        Graphics g = getBufferedImage().getGraphics();
        Graphics2D g2 = (Graphics2D) g;
        drawMap(g2); 
        projectileManager.draw(g2);
        robotManager.draw(g2);
        if(drawHiboxes) {
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
        switch(code){
                
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

            case KeyEvent.VK_LEFT:
                megaMan.attack();
                break;
                
            case KeyEvent.VK_F1:
                drawHiboxes = !drawHiboxes;
                break;
        }
    }

    public void setReleasedButton(int code) {
        switch(code){
                
            case KeyEvent.VK_D:
                if(megaMan.getSpeedX() > 0)
                    megaMan.stopRun();
                break;
                
            case KeyEvent.VK_A:
                if(megaMan.getSpeedX() < 0)
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
