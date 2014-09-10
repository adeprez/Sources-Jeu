package editeurMap;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import listeners.SelectionObjetListener;
import map.objets.Objet;
import statique.Style;

import composants.panel.PanelImage;

import divers.Outil;

public class IconeObjet extends PanelImage implements MouseListener {
	private static final long serialVersionUID = 1L;
	private final Objet objet;
	private boolean survol;


	public IconeObjet(Objet objet) {
		super(objet.getImage());
		setPreferredSize(new Dimension(64, 64));
		setToolTipText(objet.getNom());
		this.objet = objet;
		addMouseListener(this);
	}

	public Objet getObjet() {
		return objet;
	}
	
	public void addSelectionObjetListener(SelectionObjetListener l) {
		listenerList.add(SelectionObjetListener.class, l);
	}
	
	public void removeSelectionObjetListener(SelectionObjetListener l) {
		listenerList.remove(SelectionObjetListener.class, l);
	}
	
	public void setSurvol(boolean survol) {
		this.survol = survol;
		repaint();
	}
	
	protected void notifySelectionObjetListener(boolean cliqueDroit) {
		for(final SelectionObjetListener l : listenerList.getListeners(SelectionObjetListener.class))
			l.selection(objet, cliqueDroit);
	}
	
	public void dessineTexte(Graphics g, int y, String str) {
		g.drawString(str, (getWidth() - g.getFontMetrics().stringWidth(str))/2, y);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		if(survol) {
			g.setFont(Style.POLICE.deriveFont(12f));
			dessineTexte(g, getHeight() - 10, "(" + objet.getType().getNom() + ")");
			g.setColor(new Color(255, 255, 255, 100));
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(Color.BLACK);
			dessineTexte(g, 15, objet.getNom());
		}
	}

	@Override 
	public void mouseClicked(MouseEvent e) {
		notifySelectionObjetListener(Outil.estCliqueDroit(e));
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		setSurvol(true);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		setSurvol(false);
	}
	
	@Override public void mousePressed(MouseEvent e) {}
	@Override public void mouseReleased(MouseEvent e) {}
	
}
