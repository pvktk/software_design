package roguelike;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;

/*
 * На этой панели всё рисуется.
 */
class MyPanel extends JPanel {
	
	private List<Drawable> ld;
	private int width, height;
	
	private Labirint lb;
	
	MyPanel (Labirint l) {
		lb = l;
		
		ld = l.getListDraw();
		width = l.getWidth();
		height = l.getHeight();
		
		setFocusable(true);
		requestFocusInWindow();
		
		addKeyListener(l.getMainBot());
	}
	
	@Override
	public Dimension getPreferredSize() {
		// TODO Auto-generated method stub
		return new Dimension(width * RogueStarter.boxW, height * RogueStarter.boxH);
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 * При перерисовке рисуем все рисуемые объекты
	 */
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		for (Drawable d : ld) {
			d.drawSelf(g);
		}
		
	}
}
