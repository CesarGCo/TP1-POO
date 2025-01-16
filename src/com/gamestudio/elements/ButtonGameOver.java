package com.gamestudio.elements;

import java.awt.Color;
import java.awt.Graphics;

import com.gamestudio.effect.FrameImage;
import com.gamestudio.manager.DataLoader;

public class ButtonGameOver {
	protected int x;
	protected int y;
	protected boolean enabled;
	protected FrameImage frameImage;

	public ButtonGameOver(int x, int y) {
		this.x = x;
		this.y = y;
		this.enabled = false;
		this.frameImage = DataLoader.getInstance().getFrameImage("arrow_game_over");
	}
	public boolean isEnabled() {
		return enabled;
	}

	public void update() {
		this.enabled = !enabled;
	}


	public void draw(Graphics g) {
		if(enabled) {
			g.drawImage(frameImage.getImage(), x, y, null);
		} else {
			g.setColor(Color.black);
			g.fillRect(x, y, 22, 30);
		}
	}
}
