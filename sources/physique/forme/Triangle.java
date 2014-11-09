package physique.forme;

import interfaces.Localise;
import io.IO;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;

import physique.Collision;
import vision.Orientation;

public class Triangle extends Forme {
    private final boolean estDecoupe;
    private final Rectangle forme;
    private Polygon triangle, triangleDessin;
    private Rectangle zone;


    public Triangle(Orientation orientation, boolean estDecoupe) {
	this(orientation, estDecoupe, new Rectangle(Localise.UNITE.width, Localise.UNITE.height));
    }

    public Triangle(Orientation orientation, boolean estDecoupe, Rectangle forme) {
	super(orientation);
	this.estDecoupe = estDecoupe;
	this.forme = forme;
    }

    public Triangle(IO io) {
	this(Orientation.get(io.nextPositif()), io.nextBoolean());
    }

    public Triangle(Rectangle forme, boolean decoupe) {
	this(Orientation.DROITE, decoupe, forme);
    }

    public Polygon getTriangle() {
	if(triangle == null)
	    triangle = new Polygon(getOrientation().versDroite() ?
		    new int[] {forme.x, forme.x + forme.width, forme.x}	:
			new int[] {forme.x + forme.width, forme.x, forme.x + forme.width}, getOrientation().retourne() ?
				new int[] {forme.y, forme.y + forme.height, forme.y + forme.height} :
				    new int[] {forme.y + forme.height, forme.y, forme.y}, 3);
	return triangle;
    }

    @Override
    public int getYGaucheHaut(Rectangle zone) {
	return getOrientation() == Orientation.GAUCHE ? zone.y + zone.height : super.getYGaucheHaut(zone);
    }

    @Override
    public int getYGaucheBas(Rectangle zone) {
	return getOrientation() == Orientation.GAUCHE_BAS ? zone.y : super.getYGaucheBas(zone);
    }

    @Override
    public int getYDroiteHaut(Rectangle zone) {
	return getOrientation() == Orientation.DROITE ? zone.y + zone.height : super.getYDroiteHaut(zone);
    }

    @Override
    public int getYDroiteBas(Rectangle zone) {
	return getOrientation() == Orientation.DROITE_BAS ? zone.y : super.getYDroiteBas(zone);
    }

    @Override
    public int getX() {
	return forme.x;
    }

    @Override
    public int getY() {
	return forme.y;
    }

    @Override
    public int getLargeur() {
	return forme.width;
    }

    @Override
    public int getHauteur() {
	return forme.height;
    }

    @Override
    public TypeForme getType() {
	return TypeForme.TRIANGLE;
    }

    @Override
    public Triangle creerCopie() {
	return (Triangle) Forme.get(sauvegarder(new IO()));
    }

    @Override
    public Shape getDecoupe(Rectangle zone) {
	if(triangleDessin == null || !zone.equals(this.zone)) {
	    this.zone = zone;
	    triangleDessin = new Polygon(getOrientation().versDroite() ?
		    new int[] {zone.x, zone.x + zone.width, zone.x} :
			new int[] {zone.x + zone.width, zone.x, zone.x + zone.width}, getOrientation().retourne() ?
				new int[] {zone.y + zone.height, zone.y, zone.y} :
				    new int[] {zone.y, zone.y + zone.height, zone.y + zone.height}, 3);
	}
	return triangleDessin;
    }

    @Override
    public void setOrientation(Orientation orientation) {
	super.setOrientation(orientation);
	triangleDessin = null;
	triangle = null;
    }

    @Override
    public Collision setLargeur(int largeur) {
	forme.width = largeur;
	triangleDessin = null;
	triangle = null;
	return null;
    }

    @Override
    public Collision setHauteur(int hauteur) {
	forme.height = hauteur;
	triangleDessin = null;
	triangle = null;
	return null;
    }

    @Override
    public Rectangle getRectangle() {
	return forme;
    }

    @Override
    public void setXForme(int x) {
	forme.x = x;
	triangleDessin = null;
	if(triangle != null)
	    triangle.translate(getX() - x, 0);
    }

    @Override
    public void setYForme(int y) {
	forme.y = y;
	triangleDessin = null;
	if(triangle != null)
	    triangle.translate(0, getY() - y);
    }

    @Override
    public IO sauvegarder(IO io) {
	return super.sauvegarder(io).addBytePositif(getOrientation().ordinal()).add(estDecoupe);
    }

    @Override
    public String toString() {
	return "Triangulaire " + (estDecoupe ? "decoupe " : "") + super.toString();
    }

    @Override
    public boolean estDecoupe() {
	return estDecoupe;
    }

    @Override
    public boolean intersection(Forme autre) {
	if(autre.getType() == TypeForme.TRIANGLE)
	    return ((Triangle) autre).getTriangle().intersects(forme) && getTriangle().intersects(((Triangle) autre).forme);
	if(autre.getType() == TypeForme.POINT)
	    return forme.contains(autre.getX(), autre.getY()) && getTriangle().contains(autre.getX(), autre.getY());
	return forme.intersects(autre.getRectangle()) && getTriangle().intersects(autre.getRectangle());
    }

}
