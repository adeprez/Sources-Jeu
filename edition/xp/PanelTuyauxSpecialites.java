package xp;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import ressources.Images;
import specialite.competence.Competence;

public class PanelTuyauxSpecialites extends JPanel {
	private static final long serialVersionUID = 1L;
	private final PanelSpecialite[] specialites;
	private final BufferedImage couvercle;

	
	public PanelTuyauxSpecialites(PanelSpecialite... specialites) {
		this.specialites = specialites;
		couvercle = Images.get(Competence.PATH + "couvercle.png", true);
		setOpaque(false);
	}
	
	public void dessinerHorizontal(Graphics2D g, int w, PanelSpecialite p) {
		int y = p.getY() + (p.getHeight() - w)/2;
		g.setPaint(new GradientPaint(0, y, Color.BLACK, 0, y + w/2, PanelSpecialite.COULEUR_CUIVRE, true));
		g.fillRoundRect(w/2, y, getWidth(), w, w, w);
		g.drawImage(couvercle, getWidth() - w/2, p.getY(), w/2, p.getHeight(), null);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		PanelSpecialite dernier = specialites[specialites.length - 1];
		int w = getWidth()/2;
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setPaint(new GradientPaint(0, 0, Color.BLACK, w/2, 0, PanelSpecialite.COULEUR_CUIVRE, true));
		g.fillRoundRect(0, -w, w, dernier.getY() + dernier.getHeight()/2 +  (3 * w)/2, w, w);
		for(final PanelSpecialite p : specialites)
			dessinerHorizontal(g2, w, p);
	}
	
}
