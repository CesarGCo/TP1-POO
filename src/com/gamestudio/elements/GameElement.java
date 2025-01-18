package com.gamestudio.elements;
import com.gamestudio.state.GameState;

public abstract class GameElement {
    private float posX;
	private float posY;
	
	private GameState gameState;
	
	public GameElement(float x, float y, GameState gameState){
		this.posX = x;
		this.posY = y;
		this.gameState = gameState;
	}
	
	public void setPosX(float x){
		posX = x;
	}
	
	public float getPosX(){
		return posX;
	}
	
	public void setPosY(float y){
		posY = y;
	}
	
	public float getPosY(){
		return posY;
	}
	
	public GameState getGameState(){
		return gameState;
	}
	
	public abstract void update();
}
