package com.gamestudio.elements;

import java.awt.Rectangle;

import com.gamestudio.state.GameState;

// Essa Classe representa alguma entidade inteligente no jogo
// isto é, entidades que possuem lógica mais avançada, como disparar, maior número de animações e ações diferentes
// Exemplo: Mega Man e Wood Man
public abstract class SmartGameEntity extends GameEntity {
    private boolean isJumping; // Caso esteja pulando
    private boolean isLanding; // Caso esteja caindo

    public SmartGameEntity(int x, int y, int width, int height, float mass, int amountLife, GameState gameState) {
        super(x, y, width, height, mass, amountLife, gameState);
        setCurrentState(ALIVE);
    }

    public abstract void run(); // Movimento a entidade de uma forma específica
    
    public abstract void jump(); // Executa a ação de pular da entidade de forma específica
    
    public abstract void stopRun(); // Zera a velocidade horizontal da entidade

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

    public abstract void attack(); // Realiza a ação de ataque da entidade
    
    // Atualiza todos os parâmetros da entidade com base no seu estado atual
    @Override
    public void update(){
        super.update();
        
        if(getCurrentState() == ALIVE){
            if(!isLanding) {
                setPosX(getPosX() + getSpeedX());
                if(getGameState().physicalMap.haveCollisionWithLeftWall(getBoundForCollisionWithMap())!=null){

                    Rectangle rectLeftWall = getGameState().physicalMap.haveCollisionWithLeftWall(getBoundForCollisionWithMap());
                    setPosX(rectLeftWall.x + rectLeftWall.width + (float) getWidth() /2);

                } else if(getGameState().physicalMap.haveCollisionWithRightWall(getBoundForCollisionWithMap())!=null){

                    Rectangle rectRightWall = getGameState().physicalMap.haveCollisionWithRightWall(getBoundForCollisionWithMap());
                    setPosX(rectRightWall.x - (float) getWidth() /2);

                }

                Rectangle boundForCollisionWithMapFuture = getBoundForCollisionWithMap();
                boundForCollisionWithMapFuture.y += getSpeedY() != 0? (int) getSpeedY() : 2;
                Rectangle rectLand = getGameState().physicalMap.haveCollisionWithLand(boundForCollisionWithMapFuture, this);
                Rectangle rectTop = getGameState().physicalMap.haveCollisionWithTop(boundForCollisionWithMapFuture);
                
                if(rectTop !=null){
                    
                    setSpeedY(0);
                    setPosY((int) (rectTop.y + getGameState().physicalMap.getTileSize() + getHeight()/2));
                    
                }else if(rectLand != null){
                    setIsJumping(false);
                    setSpeedY(0);
                    setPosY(rectLand.y - (float) getHeight() /2);
                }else{
                    setIsJumping(true);
                    setSpeedY((getSpeedY() + getMass()));
                    setPosY((int) (getPosY() + getSpeedY()));
                }
            }
        }
    }
}
