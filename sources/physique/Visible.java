package physique;

import interfaces.ContaineurImage;
import interfaces.LocaliseDessinable;
import interfaces.Marquable;
import interfaces.Masquable;
import interfaces.Nomme;
import io.IO;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;

import physique.forme.Forme;
import ressources.Proprietes;
import statique.Style;
import vision.Camera;


public abstract class Visible extends PhysiqueDestructible implements LocaliseDessinable, Nomme, Masquable, ContaineurImage, Marquable {
    private boolean visible, selectionne;


    public Visible(Forme forme) {
	super(forme);
	setVisible(true);
    }

    public abstract Color getCouleur();
    public abstract boolean aFond();
    public abstract BufferedImage getImageFond();

    public boolean estSelectionne() {
	return selectionne;
    }

    public void setSelectionne(boolean selectionne) {
	this.selectionne = selectionne;
    }

    @Override
    public IO getPaquetVie() {
	return new IO();
    }

    @Override
    public void setMarque(boolean marque) {
	selectionne = marque;
    }

    @Override
    public boolean estVisible() {
	return visible;
    }

    @Override
    public void setVisible(boolean visible) {
	this.visible = visible;
    }

    @Override
    public void dessiner(Graphics2D g, Rectangle zone, int equipe) {
	if(estVisible()) {
	    BufferedImage img = getImage();
	    dessiner(g, zone, img);
	}
    }

    public void dessinerFond(Graphics2D g, Rectangle zone) {
	if(estVisible() && aFond())
	    g.drawImage(getImageFond(), zone.x, zone.y, zone.width, zone.height, null);
    }

    public void dessiner(Graphics2D g, Rectangle zone, BufferedImage image) {
	dessiner(g, image, zone, getForme(), getForme().estDecoupe());
    }

    @Override
    public void predessiner(Graphics2D g, Rectangle zone, int equipe) {
	dessinerFond(g, zone);
    }

    @Override
    public void surdessiner(Graphics2D g, Rectangle zone, int equipe) {
	if(selectionne) {
	    g.setColor(new Color(255, 255, 255, 100));
	    g.fill(getForme().getDecoupe(zone));
	}
	if(Proprietes.getInstance().estDebug()) {
	    g.setColor(Color.BLACK);
	    g.setFont(Style.POLICE);
	    String s1 = getNom(), s2 = getX() + ";" + getY();
	    g.drawString(s1, zone.x + (zone.width - g.getFontMetrics().stringWidth(s1))/2, zone.y + zone.height/2);
	    g.drawString(s2, zone.x + (zone.width - g.getFontMetrics().stringWidth(s2))/2, zone.y + zone.height/2 + 15);
	}
    }

    @Override
    public void dessiner(Camera c, Graphics2D gPredessin, Graphics2D gDessin, Graphics2D gSurdessin) {
	Rectangle zone = c.getZone(this, 0);
	int equipe = c.getSource().getEquipe();
	predessiner(gPredessin, zone, equipe);
	dessiner(gDessin, zone, equipe);
	surdessiner(gSurdessin, zone, equipe);
    }

    @Override
    public String toString() {
	return super.toString() + "[VISIBLE : " + (estVisible() ? "oui" : "non" + ")]");
    }

    public static void dessiner(Graphics g, Image image, Rectangle zone, Forme forme, boolean decoupe) {
	Shape tmp = g.getClip();
	if(decoupe)
	    g.setClip(forme.getDecoupe(zone));
	switch(forme.getOrientation()) {
	case DROITE:
	    g.drawImage(image, zone.x, zone.y, zone.width, zone.height, null);
	    break;
	case GAUCHE:
	    g.drawImage(image, zone.x + zone.width, zone.y, -zone.width, zone.height, null);
	    break;
	case DROITE_BAS:
	    g.drawImage(image, zone.x, zone.y + zone.height, zone.width, -zone.height, null);
	    break;
	case GAUCHE_BAS:
	    g.drawImage(image, zone.x + zone.width, zone.y + zone.height, -zone.width, -zone.height, null);
	    break;
	}
	g.setClip(tmp);
    }

}
