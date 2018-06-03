package roguelike;

/*
 * Базовый класс для ботов в игре. Есть параметр жизнь.
 */
class Bot extends WithCoordsAndSpeed {
	public Bot(int x, int y) {
		super(x, y);

	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	private int life;
}
