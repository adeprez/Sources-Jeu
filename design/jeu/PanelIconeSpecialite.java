package jeu;

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
import listeners.ChangeSpecialiteListener;
import ressources.Images;
import specialite.Specialite;
import specialite.competence.Competence;

import composants.panel.PanelImage;

public class PanelIconeSpecialite extends JPanel implements MouseListener {
	private static final long serialVersionUID = 1L;
	private final ChangeSpecialiteListener l;
	private final BufferedImage img;
	private final Specialite spe;
	private boolean survol, select;


	public PanelIconeSpecialite(ChangeSpecialiteListener l, Specialite spe) {
		this.spe = spe;
		this.l = l;
		setOpaque(false);
		img = Images.get(Competence.PATH + "cadre.png", true);
		PanelImage p = new PanelImage(spe.getIcone());
		setLayout(new LayoutPrctCentre(75, 75));
		add(p);
		addMouseListener(this);
	}

	public Specialite getSpecialite() {
		return spe;
	}

	public void setSelectionne(boolean select) {
		this.select = select;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Rectangle r = new Rectangle(getWidth()/10, getHeight()/10, getWidth() - getWidth()/5, getHeight() - getHeight()/5);
		if(survol) {
			g.setColor(new Color(255, 255, 255, 100));
			((Graphics2D) g).fill(r);
		}
		if(!select) {
			g.setColor(new Color(0, 0, 0, 175));
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
		l.changeSpecialite(spe.getSource(), spe.getSource().getSpecialite(), spe);
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

}
