package vision;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import listeners.SelectionListener;
import listeners.SelectionObjetListener;
import map.Map;
import map.objets.Objet;
import physique.Collision;
import statique.Style;
import divers.Outil;
import exceptions.HorsLimiteException;

public class ReticuleObjet extends ReticuleSelection implements SelectionObjetListener {
	private Objet objet;
	private final Map map;
	private boolean dessin;


	public ReticuleObjet(Map map) {
		this.map = map;
		setDessin(true).setCouleur(new Color(50, 50, 50, 50)).setTaille(1);
	}

	public ReticuleObjet(Map map, Camera camera) {
		this(map);
		setCamera(camera);
	}
	
	public ReticuleObjet setDessin(boolean dessin) {
		this.dessin = dessin;
		return this;
	}
	
	public void addSelectionObjetListener(SelectionObjetListener l) {
		addListener(SelectionObjetListener.class, l);
	}
	
	public void removeSelectionObjetListener(SelectionObjetListener l) {
		removeListener(SelectionObjetListener.class, l);
	}
	
	protected void notifySelectionObjetListener(Objet o, boolean cliqueDroit) {
		for(final SelectionObjetListener l : getListeners(SelectionObjetListener.class))
			l.selection(o, cliqueDroit);
	}
	
	public void addSelectionListener(SelectionListener l) {
		addListener(SelectionListener.class, l);
	}
	
	public void removeSelectionListener(SelectionListener l) {
		removeListener(SelectionListener.class, l);
	}
	
	protected void notifySelectionListener(int x, int y, boolean cliqueDroit) {
		for(final SelectionListener l : getListeners(SelectionListener.class))
			l.selection(x, y, cliqueDroit);
	}
	
	@Override
	public void dessiner(Graphics2D g, Rectangle zone, int equipe) {
		super.dessiner(g, zone, equipe);
		if(dessin && objet != null && estVisible()) {
			Composite tmp = g.getComposite();
			g.setComposite(Style.TRANSPARENCE);
			objet.predessiner(g, zone, equipe);
			objet.dessiner(g, zone, equipe);
			g.setComposite(tmp);
		}
	}

	@Override
	public void clique(MouseEvent e) {
		super.clique(e);
		try {
			notifySelectionObjetListener(map.getObjet(this), Outil.estCliqueDroit(e));
		} catch(HorsLimiteException err) {
			notifySelectionListener(getCamera().revertX(e.getX()), getCamera().revertY(e.getY()), Outil.estCliqueDroit(e));
		}
	}

	@Override
	public void selection(Objet objet, boolean cliqueDroit) {
		this.objet = objet;
	}
	
	@Override
	public int getX() {
		return objet == null ? super.getX() : objet.getX();
	}

	@Override
	public int getY() {
		return objet == null ? super.getY() : objet.getY();
	}

	@Override
	public Collision setX(int x) {
		if(objet == null)
			return super.setX(x);
		else return objet.getForme().setX(x);
	}

	@Override
	public Collision setY(int y) {
		if(objet == null)
			return super.setY(y);
		else return objet.getForme().setY(y);
	}

	@Override
	public int getLargeur() {
		return objet == null ? UNITE.width : objet.getLargeur();
	}

	@Override
	public int getHauteur() {
		return objet == null ? UNITE.height : objet.getHauteur();
	}
	

}
