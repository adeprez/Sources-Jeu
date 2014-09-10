package carte.ui;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import listeners.ControleMapListener;
import statique.Style;

import composants.panel.PanelImage;

import controles.TypeAction;

public class PrismeControleDirection extends PanelImage implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;
	private final ControleMapListener l;
	private final Polygon[] formes;
	private TypeAction action;
	private Color color;
	private Polygon forme;


	public PrismeControleDirection(ControleMapListener l) {
		super("divers/control.png");
		this.l = l;
		setCursor(Style.curseur.main());
		formes = new Polygon[4];
		tailleImage();
		setFormes();
		addMouseListener(this);
		addMouseMotionListener(this);
		color = Color.BLACK;
	}
	
	public void setFormes() {
		formes[0] = getForme(TypeAction.GAUCHE);
		formes[1] = getForme(TypeAction.DROITE);
		formes[2] = getForme(TypeAction.ACCROUPI);
		formes[3] = getForme(TypeAction.ESCALADER);
	}

	public void setDirection(TypeAction action) {
		forme = action == null ? null : getForme(action);
	}
	
	public Polygon getForme(TypeAction action) {
		switch(action) {
		case GAUCHE: return new Polygon(
				new int[] {0, 0, getPreferredSize().width/2, 0}, 
				new int[] {0, getPreferredSize().height, getPreferredSize().height/2, 0}, 4);
		case DROITE: return new Polygon(
				new int[] {getPreferredSize().width, getPreferredSize().width, getPreferredSize().width/2, getPreferredSize().width}, 
				new int[] {0, getPreferredSize().height, getPreferredSize().height/2, 0}, 4);
		case ACCROUPI: return new Polygon(
				new int[] {0, getPreferredSize().width, getPreferredSize().width/2, 0}, 
				new int[] {getPreferredSize().height, getPreferredSize().height, getPreferredSize().height/2, getPreferredSize().height}, 4);
		case ESCALADER: return new Polygon
				(new int[] {0, getPreferredSize().width, getPreferredSize().width/2, 0}, new int[] {0, 0, getPreferredSize().height/2, 0}, 4);
		default: return new Polygon();
		}
	}

	public TypeAction getDirection(MouseEvent e) {
		if(formes[0].contains(e.getPoint()))
			return TypeAction.GAUCHE;
		if(formes[1].contains(e.getPoint()))
			return TypeAction.DROITE;
		if(formes[2].contains(e.getPoint()))
			return TypeAction.ACCROUPI;
		if(formes[3].contains(e.getPoint()))
			return TypeAction.ESCALADER;
		return null;
	}
	
	public TypeAction survol(MouseEvent e) {
		TypeAction a = getDirection(e);
		setToolTipText(a == null ? null : a.getDirection());
		return a;
	}
	
	@Override public void mouseEntered(MouseEvent e) {}
	@Override public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {
		setDirection(null);
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		action = survol(e);
		setDirection(action);
		l.appuie(action);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		color = Color.BLUE;
		setDirection(survol(e));
	}

	@Override 
	public void mouseReleased(MouseEvent e) {
		color = Color.BLUE;
		l.relache(action);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		color = Color.BLACK;
		action = getDirection(e);
		setToolTipText(null);
		setDirection(action);
		l.appuie(action);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Composite tmp = ((Graphics2D) g).getComposite();
		((Graphics2D) g).setComposite(Style.TRANSPARENCE);
		g.setColor(color);
		if(forme != null)
			g.fillPolygon(forme);
		((Graphics2D) g).setComposite(tmp);
	}

}
