package com.gamestudio.elements;

import java.awt.Color;
import java.awt.Graphics;
import com.gamestudio.effect.FrameImage;
import com.gamestudio.manager.DataLoader;

public class ButtonMenu {
	protected int x;
	protected int y;
	protected boolean enabled;
	protected FrameImage frameImage;

	public ButtonMenu(int x, int y) {
		this.x = x;
		this.y = y;
		this.enabled = false;
		this.frameImage = DataLoader.getInstance().getFrameImage("arrow_menu");
	}

	public boolean isEnabled() {
		return enabled;
	}

	// Atualiza o estado do botão
	public void update() {
		this.enabled = !enabled;
	}

	// Desenha o Botão na tela
	public void draw(Graphics g) {
		if(enabled) {
			g.drawImage(frameImage.getImage(), x, y, null);
		} else {
			g.setColor(Color.black);
			g.fillRect(x, y, 39, 48);
		}
	}
}
