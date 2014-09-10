package map;

import interfaces.Dessinable;
import interfaces.Dessineur;
import interfaces.Fermable;
import interfaces.LocaliseDessinable;
import interfaces.StyleListe;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import listeners.AjoutPersoListener;
import listeners.ChangePersoListener;
import listeners.RemovePersoListener;
import perso.Perso;
import vision.Camera;
import divers.Listenable;
import ecrans.ApercuMap;
import exceptions.HorsLimiteException;

public abstract class MapDessinable<E extends LocaliseDessinable> extends Listenable
implements Dessineur, Fermable, StyleListe, ChangePersoListener, AjoutPersoListener, RemovePersoListener, DessineurElementMap<E> {
    private final List<LocaliseDessinable> dessinables;
    private final VolatileImage predessin, dessin, surdessin;
    private final Graphics2D gPredessin, gDessin, gSurdessin;
    private DessineurElementMap<E> dessineur;


    public MapDessinable() {
	dessinables = new ArrayList<LocaliseDessinable>();
	Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	predessin = getVolatileImage(d.width, d.height);
	dessin = getVolatileImage(d.width, d.height);
	surdessin = getVolatileImage(d.width, d.height);
	gPredessin = predessin.createGraphics();
	gDessin = dessin.createGraphics();
	gSurdessin = surdessin.createGraphics();
	Color c = new Color(0, 0, 0, 0);
	gPredessin.setBackground(c);
	gDessin.setBackground(c);
	gSurdessin.setBackground(c);
	setHauteQualite(false);
	dessineur = this;
    }

    public abstract boolean dansMap(int x);
    public abstract int getNombreObjets();
    public abstract String getSuperficie();
    public abstract int getLargeur();
    public abstract int getAltitude(int x) throws HorsLimiteException;
    public abstract E getObjet(int x, int y) throws HorsLimiteException;

    public void setHauteQualite(boolean hd) {
	Object r1, r2, r3;
	if(hd) {
	    r1 = RenderingHints.VALUE_ANTIALIAS_ON;
	    r2 = RenderingHints.VALUE_COLOR_RENDER_QUALITY;
	    r3 = RenderingHints.VALUE_RENDER_QUALITY;
	} else {
	    r1 = RenderingHints.VALUE_ANTIALIAS_OFF;
	    r2 = RenderingHints.VALUE_COLOR_RENDER_SPEED;
	    r3 = RenderingHints.VALUE_RENDER_SPEED;
	}
	for(final Graphics2D g : new Graphics2D[]{gPredessin, gDessin, gSurdessin}) {
	    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, r1);
	    g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, r2);
	    g.setRenderingHint(RenderingHints.KEY_RENDERING, r3);
	}
    }

    public void setDessineur(DessineurElementMap<E> dessineur) {
	this.dessineur = dessineur;
    }

    public VolatileImage getPredessin() {
	return predessin;
    }

    public VolatileImage getDessin() {
	return dessin;
    }

    public VolatileImage getSurdessin() {
	return surdessin;
    }

    public Graphics2D getgPredessin() {
	return gPredessin;
    }

    public Graphics2D getgDessin() {
	return gDessin;
    }

    public Graphics2D getgSurdessin() {
	return gSurdessin;
    }

    public void setCouleurFond(Color c) {
	gPredessin.setBackground(c);
    }

    public void setMasqueFond(Color c) {
	gDessin.setBackground(c);
    }

    public void setMasqueCentre(Color c) {
	gSurdessin.setBackground(c);
    }

    public List<LocaliseDessinable> getDessinables() {
	return dessinables;
    }

    public void dessiner(Graphics g, Camera c) {
	effaceTampons(c.getEcran());
	try {
	    c.nouvelleFrame();
	    dessineMap(c);
	    dessineDessinables(c);
	} catch(Exception e) {
	    e.printStackTrace();
	}
	dessineTampons(g);
    }

    public void dessineDessinables(Camera c) {
	for(int i=0 ; i<dessinables.size() ; i++) try {
	    LocaliseDessinable d = dessinables.get(i);
	    if(c.estVisible(d, 0))
		dessineur.dessinerDessinable(gPredessin, gDessin, gSurdessin, d, c);
	} catch(Exception err) {
	    err.printStackTrace();
	}
    }

    public void effacerFond(Component c) {
	gPredessin.clearRect(0, 0, c.getWidth(), c.getHeight());
    }

    public void effaceTampons(Component c) {
	effacerFond(c);
	gDessin.clearRect(0, 0, c.getWidth(), c.getHeight());
	gSurdessin.clearRect(0, 0, c.getWidth(), c.getHeight());
    }

    public void dessineTampons(Graphics g) {
	g.drawImage(predessin, 0, 0, null);
	g.drawImage(dessin, 0, 0, null);
	g.drawImage(surdessin, 0, 0, null);
    }

    public void dessineMap(Camera c) throws HorsLimiteException {
	int min = c.getMinX(), max = Math.min(c.getMaxX(), getLargeur()), centre = Map.checkX(c.getX());
	for(int x=max ; x>centre ; x--)
	    dessinerColonne(c, x);
	for(int x=min ; x<=centre ; x++)
	    dessinerColonne(c, x);
    }

    public void dessinerColonne(Camera c, int x) throws HorsLimiteException {
	if(dansMap(x)) {
	    int alt = getAltitude(x);
	    if(alt > 0) {
		int min = c.getMinY(), max = Math.min(c.getMaxY(), alt) - 1, centre = Map.checkY(c.getY());
		for(int y=max ; y>centre ; y--)
		    dessiner(x, y, c);
		for(int y=min ; y<=centre && y < alt ; y++)
		    dessiner(x, y, c);
	    }
	}
    }

    public void dessiner(int x, int y, Camera c) throws HorsLimiteException {
	E e = getObjet(x, y);
	if(e != null)
	    dessineur.dessinerElementMap(gPredessin, gDessin, gSurdessin, e, c);
    }

    public E getObjet(MouseEvent e, Camera c) throws HorsLimiteException {
	return getObjet(c.revertX(e.getX()), c.revertY(e.getY()));
    }

    @Override
    public void dessinerDessinable(Graphics2D gPredessin, Graphics2D gDessin, Graphics2D gSurdessin, LocaliseDessinable e, Camera c) {
	e.dessiner(c, gPredessin, gDessin, gSurdessin);
    }

    @Override
    public void dessinerElementMap(Graphics2D gPredessin, Graphics2D gDessin, Graphics2D gSurdessin, E e, Camera c) {
	int equipe = c.getSource().getEquipe();
	Rectangle fond = c.getZoneFond(e, 0);
	e.predessiner(gPredessin, fond, equipe);
	e.dessiner(gDessin, c.getZone(e, 0), equipe);
	e.surdessiner(gSurdessin, fond, equipe);
    }

    @Override
    public void ajoutDessinable(LocaliseDessinable dessinable) {
	dessinables.add(dessinable);
    }

    @Override
    public void removeDessinable(LocaliseDessinable dessinable) {
	dessinables.remove(dessinable);
    }

    @Override
    public boolean fermer() {
	for(final Dessinable d : dessinables)
	    if(d instanceof Fermable)
		((Fermable) d).fermer();
	dessinables.clear();
	return true;
    }

    @Override
    public JComponent creerVue() {
	return new ApercuMap<E>(this, false);
    }

    @Override
    public void change(Perso ancien, Perso nouveau) {
	remove(ancien);
	ajout(nouveau);
    }

    public static final BufferedImage getImage(int width, int height) {
	return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()
		.createCompatibleImage(width, height, VolatileImage.TRANSLUCENT);
    }

    public static final VolatileImage getVolatileImage(int width, int height) {
	return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()
		.createCompatibleVolatileImage(width, height, VolatileImage.TRANSLUCENT);
    }

}
