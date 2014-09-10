package composants.editeurImage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class PanelDessin extends JPanel {
	private static final long serialVersionUID = 1L;
	private final BufferedImage image;
	private final Graphics2D g;
	private int taille;
	
	
	public PanelDessin(BufferedImage image) {
		this.image = image;
		setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
		setSize(new Dimension(image.getWidth(), image.getHeight()));
		setMaximumSize(new Dimension(image.getWidth(), image.getHeight()));
		setMinimumSize(new Dimension(image.getWidth(), image.getHeight()));
		taille = 5;
		g = image.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		MouseAdapter ma = new DessinImageListener(this);
		addMouseListener(ma);
		addMouseMotionListener(ma);
	}

	public void setCouleur(Color couleur) {
		g.setColor(couleur);
	}
	
	public void setTaille(int taille) {
		this.taille = taille;
	}

	public int getTaille() {
		return taille;
	}
	
	public void dessiner(Point p) {
		g.fillOval(p.x - taille/2, p.y - taille/2, taille, taille);
		repaint(p.x - 2, p.y - 2, taille + 4, taille + 4);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, null);
	}

}
