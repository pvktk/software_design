package roguelike;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import roguelike.MainHeroBot.Weapon;

/*
 * Класс главного бота, которым мы управляем в игре
 */
class MainHeroBot extends Bot implements KeyListener, Drawable{
	//Положение оружия относительно бота
	private int weaponX, weaponY;
	final static int weaponWidth = 5, maxLife = 800, decLife = 10;
	
	enum Weapon {Shield, Knife};
	
	Weapon weapon = Weapon.Shield;
	
	MainHeroBot(int x, int y) {
		super(x, y);
		setLife(maxLife);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void drawSelf(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(getX() * RogueStarter.boxW, getY() * RogueStarter.boxH, RogueStarter.boxW, RogueStarter.boxH);
		
		g.setColor(weapon == Weapon.Shield ? Color.GREEN : Color.BLACK);
		
		g.fillRect(getX() * RogueStarter.boxW + (RogueStarter.boxW - weaponWidth) / 2 + 5 * getWeaponX(),
				getY() * RogueStarter.boxH + (RogueStarter.boxH - weaponWidth) / 2 + 5 * getWeaponY(), 5, 5);
		
		g.setColor(Color.BLACK);
		g.drawString(getLife() + "", getX() * RogueStarter.boxW, (getY() + 1) * RogueStarter.boxH);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 * Ловим все нужные нажатия клавиш
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			setVy(-1);
		}
		
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			setVy(1);
		}
		
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			setVx(-1);
		}
		
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			setVx(1);
		}
		
		if (e.getKeyChar() == 'h') {
			weapon = Weapon.Shield;
		}
		
		if (e.getKeyChar() == 'k') {
			weapon = Weapon.Knife;
		}
		
		if (e.getKeyChar() == 'w') {
			setWeaponX(0);
			setWeaponY(-1);
		}
		
		if (e.getKeyChar() == 's') {
			setWeaponX(0);
			setWeaponY(1);
		}
		
		if (e.getKeyChar() == 'a') {
			setWeaponX(-1);
			setWeaponY(0);
		}
		
		if (e.getKeyChar() == 'd') {
			setWeaponX(1);
			setWeaponY(0);
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public int getWeaponX() {
		return weaponX;
	}

	public void setWeaponX(int weaponX) {
		this.weaponX = weaponX;
	}

	public int getWeaponY() {
		return weaponY;
	}

	public void setWeaponY(int weaponY) {
		this.weaponY = weaponY;
	}
	
}
