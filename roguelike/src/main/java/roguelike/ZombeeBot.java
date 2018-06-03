package roguelike;

import java.awt.Color;
import java.awt.Graphics;

/*
 * Зомби-бот
 */
class ZombeeBot extends Bot implements Drawable{
	final static int decLife = 10;
	ZombeeBot(int x, int y) {
		super(x, y);
		setLife(800);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void drawSelf(Graphics g) {
		g.setColor(getLife() > 0 ? Color.BLUE : Color.GRAY);
		g.fillRect(getX() * RogueStarter.boxW, getY() * RogueStarter.boxH, RogueStarter.boxW, RogueStarter.boxH);
		
		g.setColor(Color.BLACK);
		g.drawString(getLife() + "", getX() * RogueStarter.boxW, (getY() + 1) * RogueStarter.boxH);
	}
	
}
