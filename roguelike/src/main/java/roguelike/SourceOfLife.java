package roguelike;

import java.awt.Color;
import java.awt.Graphics;

/*
 * Класс источника жизни
 */
class SourceOfLife extends Brick {

	public SourceOfLife(int x, int y) {
		super(x, y);
		isTransparent = true;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void drawSelf(Graphics g) {
		g.setColor(Color.GREEN);
		g.fillRect(getX()*RogueStarter.boxW, getY()*RogueStarter.boxH, RogueStarter.boxW, RogueStarter.boxH);
	}
}
