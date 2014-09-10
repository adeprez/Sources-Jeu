package composants.panel;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import statique.Style;

public class PanelTransparent extends JPanel {
	private static final long serialVersionUID = 1L;
	private boolean over;


	public PanelTransparent() {
		addMouseListener(new Listener());
	}


	@Override
	public void paint(Graphics g) {
		if(over)
			super.paint(g);
		else {
			Composite tmp = ((Graphics2D) g).getComposite();
			((Graphics2D) g).setComposite(Style.TRANSPARENCE);
			super.paint(g);
			((Graphics2D) g).setComposite(tmp);
		}
	}



	private class Listener extends MouseAdapter {

		@Override
		public void mouseEntered(MouseEvent e) {
			over = true;
		}

		@Override
		public void mouseExited(MouseEvent e) {
			over = false;
		}

	}
}
