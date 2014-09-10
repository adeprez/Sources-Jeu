package physique.forme;

import io.IO;

import java.awt.Rectangle;
import java.awt.Shape;

import physique.Collision;
import vision.Orientation;

public class Rect extends Forme {
	private final Rectangle forme;

	
	public Rect() {
		this(Orientation.DROITE);
	}
	
	public Rect(Orientation orientation) {
		this(UNITE.width, UNITE.height, orientation);
	}
	
	public Rect(Rectangle forme, Orientation orientation) {
		super(orientation);
		this.forme = forme;
	}
	
	public Rect(int x, int y, int w, int h, Orientation orientation) {
		this(new Rectangle(x, y, w, h), orientation);
	}
	
	public Rect(int w, int h, Orientation orientation) {
		this(0, 0, w, h, orientation);
	}

	public Rect(IO io) {
		this(Orientation.get(io.nextPositif()));
	}

	@Override
	public Rectangle getRectangle() {
		return forme;
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
	public int getX() {
		return forme.x;
	}

	@Override
	public int getY() {
		return forme.y;
	}

	@Override
	public void setXForme(int x) {
		forme.x = x;
	}

	@Override
	public void setYForme(int y) {
		forme.y = y;
	}

	@Override
	public TypeForme getType() {
		return TypeForme.RECTANGLE;
	}

	@Override
	public Rect creerCopie() {
		return new Rect(getX(), getY(), getLargeur(), getHauteur(), getOrientation());
	}

	@Override
	public Collision setLargeur(int largeur) {
		forme.width = largeur;
		return null;
	}

	@Override
	public Collision setHauteur(int hauteur) {
		forme.height = hauteur;
		return null;
	}

	@Override
	public Shape getDecoupe(Rectangle zone) {
		return zone;
	}

	@Override
	public boolean intersection(Forme autre) {
		if(autre.getType() == TypeForme.RECTANGLE)
			return autre.getRectangle().intersects(forme);
		if(autre.getType() == TypeForme.POINT)
			return forme.contains(autre.getX(), autre.getY());
		return autre.intersection(this);
	}
	
	@Override
	public String toString() {
		return "Rectangulaire " + super.toString();
	}
	
	@Override
	public IO sauvegarder(IO io) {
		return super.sauvegarder(io).addBytePositif(getOrientation().ordinal());
	}

	@Override
	public boolean estDecoupe() {
		return false;
	}

}
