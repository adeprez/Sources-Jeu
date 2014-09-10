package xp;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import layouts.LayoutPrctCentre;
import listeners.CliquePanelCompetenceListener;
import ressources.Images;
import specialite.competence.Competence;
import statique.Style;

import composants.panel.PanelImage;

public class PanelCompetence extends JPanel implements MouseListener {
	private static final long serialVersionUID = 1L;
	private final CliquePanelCompetenceListener l;
	private BufferedImage img;
	private final Competence comp;
	private boolean actif, survol;


	public PanelCompetence(CliquePanelCompetenceListener l, Competence comp) {
		this.comp = comp;
		this.l = l;
		setOpaque(false);
		PanelImage p = new PanelImage(comp.getIcone());
		setLayout(new LayoutPrctCentre(75, 75));
		setXPManquant(0);
		add(p);
		addMouseListener(this);
	}

	public Competence getCompetence() {
		return comp;
	}

	public boolean estActif() {
		return actif;
	}
	
	public void setActif(boolean actif) {
		this.actif = actif;
		if(actif)
			setToolTipText(comp.toString());
	}

	public void setXPManquant(int manquant) {
		setActif(manquant <= 0);
		String nimg;
		if(actif)
			nimg = "cadre";
		else {
			setToolTipText(comp.toString() + " (" + manquant + " points d'expérience pour débloquer cette compétence)");
			nimg = "cadre lock";
		}
		img = Images.get(Competence.PATH + nimg + ".png", true);
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Rectangle r = new Rectangle(getWidth()/10, getHeight()/10, getWidth() - getWidth()/5, getHeight() - getHeight()/5);
		if(!estActif()) {
			g.setColor(new Color(10, 50, 100, 100));
			((Graphics2D) g).fill(r);
			if(survol) {
				String s = "Débloquer";
				g.setFont(Style.POLICE.deriveFont(getWidth()/7f));
				g.setColor(Color.WHITE);
				g.drawString(s, (getWidth() - g.getFontMetrics().stringWidth(s))/2, (int) (getHeight()/3.5));
			}
		}
		if(survol) {
			g.setColor(new Color(255, 255, 255, 100));
			((Graphics2D) g).fill(r);
		}
		g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		survol = true;
		repaint();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		survol = false;
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		l.clique(this);
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

}
