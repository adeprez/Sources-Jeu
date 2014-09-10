package ecrans;

import interfaces.LocaliseDessinable;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import layouts.LayoutPrctCentre;
import listeners.SourisListener;
import map.MapDessinable;
import vision.Camera;
import vision.DeplacementSouris;
import base.Ecran;
import base.Fenetre;
import divers.Taille;

public class ApercuMap<E extends LocaliseDessinable> extends Ecran {
	private static final long serialVersionUID = 1L;
	private final ContainerMap<E> c;
	private final JPanel p;


	public ApercuMap(MapDessinable<E> map, boolean resizable) {
		this(resizable);
		setMap(map);
	}

	public ApercuMap(boolean resizable) {
		super("fond/rouleau.png");
		setPreferredSize(new Dimension(300, 300));
		setLayout(new MonLayout());
		c = new ContainerMap<E>(new Camera(new Taille(32).setLargeurMax(128).setLargeurMin(4)));
		setName(c.getName());
		SourisListener l = new SourisListener(null, new DeplacementSouris(c.getCamera()));
		c.addMouseMotionListener(l);
		c.addMouseListener(l);
		add(c);
		if(resizable) {
			add(p = new JPanel());
			p.setOpaque(false);
			p.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
			Etirement e = new Etirement();
			p.addMouseMotionListener(e);
		} 
		else p = null;
	}

	public Camera getCamera() {
		return c.getCamera();
	}

	public MapDessinable<E> getMap() {
		return c.getMap();
	}

	public void setMap(MapDessinable<E> map) {
		c.setMap(map);
	}

	@Override
	public void afficher(Fenetre fenetre) {
		super.afficher(fenetre);
		c.afficher(fenetre);
	}

	@Override
	public boolean fermer() {
		c.fermer();
		return super.fermer();
	}


	private class MonLayout extends LayoutPrctCentre {


		public MonLayout() {
			super(93, 77);
		}

		@Override
		public void layout(Component parent, Component c, int w, int h) {
			if(c == p)
				p.setBounds(0, parent.getHeight() - h/6, parent.getWidth(), h/6);
			else super.layout(parent, c, w, h);
		}

	}

	private class Etirement extends MouseAdapter {
		private Point p;
		private int de;


		@Override
		public void mousePressed(MouseEvent e) {
			p = e.getPoint();
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if(p != null) {
				Dimension d = getPreferredSize();
				int h = d.height + e.getY() - p.y - de;
				de = d.height - h;
				setPreferredSize(new Dimension(d.width, Math.max(h, 50)));
				getParent().doLayout();
				getParent().repaint();
			}
			p = e.getPoint();
		}

	}

}
