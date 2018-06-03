package roguelike;

import java.io.IOException;

import javax.swing.JPanel;

import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
    
	public void testLabirintBorders() throws IOException, InvalidLabirintException {
		Labirint testLab = new Labirint("testlab");
		
		MainHeroBot mb = testLab.getMainBot();
		
		LabirintItemsUpdater itemUpd = new LabirintItemsUpdater(testLab, new JPanel());
		
		assertEquals(1, mb.getX());
		
		mb.setVx(1);
		
		assertEquals(0, itemUpd.IterateOnce());
		
		assertEquals(1, mb.getX());
		assertEquals(0, mb.getY());
	}
	
	public void testBrickSolidity() throws IOException, InvalidLabirintException {
		Labirint testLab = new Labirint("testlab");
		
		MainHeroBot mb = testLab.getMainBot();
		
		LabirintItemsUpdater itemUpd = new LabirintItemsUpdater(testLab, new JPanel());
		
		assertEquals(1, mb.getX());
		
		mb.setVx(-1);
		
		assertEquals(0, itemUpd.IterateOnce());
		
		assertEquals(1, mb.getX());
		assertEquals(0, mb.getY());
	}
}
