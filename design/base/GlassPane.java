package base;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import statique.Style;

public class GlassPane extends JPanel {
	private static final long serialVersionUID = 1L;
	private boolean clair, sombre;


	public GlassPane() {
		setOpaque(false);
	}

	public void setSombre(boolean sombre) {
		setVisible(sombre);
		this.sombre = sombre;
		repaint();
	}

	public void setClair(boolean clair) {
		setVisible(clair);
		this.clair = clair;
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(clair || sombre) {
			Graphics2D g2d = (Graphics2D) g;
			Composite tmp = g2d.getComposite();
			g2d.setComposite(Style.TRANSPARENCE);
			g.setColor(clair ? Color.WHITE : Color.BLACK);
			g.fillRect(0, 0, getWidth(), getHeight());
			g2d.setComposite(tmp);
		}
	}

}
