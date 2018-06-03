package roguelike;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/*
 * Объект, у которого есть координаты, скорость, и который может быть твёрдым или через него можно пройти.
 */
public class WithCoordsAndSpeed {
	private int x, y, vx, vy;
	boolean isTransparent = false;
	public WithCoordsAndSpeed(int x, int y) {
		this.setX(x);
		this.setY(y);
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getVy() {
		return vy;
	}
	public void setVy(int vy) {
		this.vy = vy;
	}
	public int getVx() {
		return vx;
	}
	public void setVx(int vx) {
		this.vx = vx;
	}
}
