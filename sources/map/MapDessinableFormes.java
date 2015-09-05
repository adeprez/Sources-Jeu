package map;

import interfaces.Actualisable;
import interfaces.Dessinable;
import interfaces.Localise;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

import javax.swing.JPanel;

import map.objets.Objet;
import physique.Mobile;
import physique.forme.Triangle;
import physique.forme.TypeForme;
import temps.AbstractPeriodique;
import base.Fenetre;

public class MapDessinableFormes extends JPanel implements Actualisable, MouseMotionListener, MouseListener {
    private static final long serialVersionUID = 1L;
    private final Dimension d;
    private final Map map;
    private Point p;


    public MapDessinableFormes(Map map) {
	this.map = map;
	d = new Dimension();
	AbstractPeriodique p = new AbstractPeriodique(50) {
	    @Override public void iteration() {
		actualise();
	    }};
	    p.lancer();
	    addMouseMotionListener(this);
	    addMouseListener(this);
    }

    public void dessiner(Graphics2D g) {
	Rectangle r = new Rectangle(Localise.UNITE);
	r.x = d.width;
	for(final List<Objet> colonne : map.getObjets()) {
	    r.y = d.height;
	    for(final Objet o : colonne) {
		Rectangle rr = (Rectangle) o.getForme().getRectangle().clone();
		rr.x += d.width;
		rr.y += d.height;
		o.predessiner(g, rr, 0);
		o.dessiner(g, r, 0);
		o.surdessiner(g, rr, 0);
		g.setColor(new Color(255, 0, 0, 50));
		Shape s = o.getForme().getType() == TypeForme.TRIANGLE ? ((Triangle) o.getForme()).getTriangle() : o.getForme().getRectangle();
		g.fill(s);
		r.y += Localise.UNITE.height;
	    }
	    r.x += Localise.UNITE.width;
	}
	for(final Dessinable dd : map.getDessinables())
	    if(dd instanceof Mobile) {
		Localise l = (Localise) dd;
		Rectangle rr = new Rectangle(l.getX() + d.width, l.getY() + d.height, l.getLargeur(), l.getHauteur());
		((Mobile) dd).getForme().dessine(g, rr);
	    }
    }

    @Override
    public void paintComponent(Graphics g) {
	super.paintComponent(g);
	dessiner((Graphics2D) g);
    }

    @Override
    public void actualise() {
	repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
	if(p != null) {
	    d.width += e.getPoint().x - p.x;
	    d.height += e.getPoint().y - p.y;
	}
	p = e.getPoint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
	p = e.getPoint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    public static Fenetre show(Map map) {
	Fenetre f = Fenetre.newFrame(new MapDessinableFormes(map));
	f.setTitle("Map (inversee)");
	f.setVisible(true);
	return f;
    }

}
