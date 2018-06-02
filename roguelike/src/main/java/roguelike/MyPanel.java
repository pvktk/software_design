package roguelike;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;

class MyPanel extends JPanel {
	
	private List<Drawable> ld;
	private int width, height;
	
	public Labirint lb;
	
	MyPanel (Labirint l) {
		lb = l;
		
		ld = l.listDraw;
		width = l.width;
		height = l.height;
		
		setFocusable(true);
		requestFocusInWindow();
		
		addKeyListener(l.mainBot);
	}
	
	@Override
	public Dimension getPreferredSize() {
		// TODO Auto-generated method stub
		return new Dimension(width*RogueStarter.boxW, height*RogueStarter.boxH);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		for (Drawable d : ld) {
			d.drawSelf(g);
		}
		
	}
}
