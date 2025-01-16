package com.gamestudio.elements;

import java.awt.Rectangle;

import com.gamestudio.state.GameState;

public abstract class SmartRobot extends Robot {
    private boolean isJumping;
    private boolean isLanding;

    public SmartRobot(int x, int y, float width, float height, float mass, int amountLife, GameState gameState) {
        super(x, y, width, height, mass, amountLife, gameState);
        setCurrentState(ALIVE);
    }

    public abstract void run();
    
    public abstract void jump();
    
    public abstract void stopRun();

    public boolean getIsJumping() {
        return isJumping;
    }
    
    public void setIsLanding(boolean b){
        isLanding = b;
    }
    
    public boolean getIsLanding(){
        return isLanding;
    }
    
    public void setIsJumping(boolean isJumping) {
        this.isJumping = isJumping;
    }

    public abstract void attack();
    
    @Override
    public void update(){
        super.update();
        
        if(getCurrentState() == ALIVE){
            if(!isLanding){
                setPosX(getPosX() + getSpeedX());
                if(getDirection() == RIGHT && 
                        getGameState().physicalMap.haveCollisionWithLeftWall(getBoundForCollisionWithMap())!=null){

                    Rectangle rectLeftWall = getGameState().physicalMap.haveCollisionWithLeftWall(getBoundForCollisionWithMap());
                    setPosX((int) (rectLeftWall.x + rectLeftWall.width + getWidth()/2));

                }
                if(getDirection() == RIGHT && 
                        getGameState().physicalMap.haveCollisionWithRightWall(getBoundForCollisionWithMap())!=null){

                    Rectangle rectRightWall = getGameState().physicalMap.haveCollisionWithRightWall(getBoundForCollisionWithMap());
                    setPosX((int) (rectRightWall.x - getWidth()/2));

                }

                Rectangle boundForCollisionWithMapFuture = getBoundForCollisionWithMap();
                boundForCollisionWithMapFuture.y += (getSpeedY()!=0?getSpeedY(): 2);
                Rectangle rectLand = getGameState().physicalMap.haveCollisionWithLand(boundForCollisionWithMapFuture);
                
                Rectangle rectTop = getGameState().physicalMap.haveCollisionWithTop(boundForCollisionWithMapFuture);
                
                if(rectTop !=null){
                    
                    setSpeedY(0);
                    setPosY((int) (rectTop.y + getGameState().physicalMap.getTileSize() + getHeight()/2));
                    
                }else if(rectLand != null){
                    setIsJumping(false);
                    if(getSpeedY() > 0) setIsLanding(true);
                    setSpeedY(0);
                    setPosY((int) (rectLand.y - getHeight()/2 - 1));
                }else{
                    setIsJumping(true);
                    setSpeedY((int) (getSpeedY() + getMass()));
                    setPosY(getPosY() + getSpeedY());
                }
            }
        }
    }
}
