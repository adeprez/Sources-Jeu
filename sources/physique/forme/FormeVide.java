package physique.forme;

import io.IO;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;

import physique.Collision;
import vision.Orientation;

public class FormeVide extends Forme {
	public static final int ID = -1;
	private final Point p;


	public FormeVide() {
		super(Orientation.DROITE);
		p = new Point();
	}
	
	public FormeVide(int x, int y) {
		this();
		setX(x);
		setY(y);
	}
	
	@Override
	public int getNoDefForme() {
		return COMPLET;
	}

	@Override
	public int getLargeur() {
		return UNITE.width;
	}

	@Override
	public int getHauteur() {
		return UNITE.height;
	}

	@Override
	public int getX() {
		return p.x;
	}

	@Override
	public int getY() {
		return p.y;
	}

	@Override
	public void setXForme(int x) {
		p.x = x;
	}

	@Override
	public void setYForme(int y) {
		p.y = y;
	}

	@Override
	public TypeForme getType() {
		return TypeForme.VIDE;
	}

	@Override
	public FormeVide creerCopie() {
		return new FormeVide(getX(), getY());
	}

	@Override
	public Collision setLargeur(int largeur) {
		return null;
	}

	@Override
	public Collision setHauteur(int hauteur) {
		return null;
	}

	@Override
	public Shape getDecoupe(Rectangle zone) {
		return zone;
	}

	@Override
	public boolean intersection(Forme autre) {
		return false;
	}
	
	@Override
	public String toString() {
		return "[Vide en " + getX() + ", " + getY() + "]";
	}

	@Override
	public Rectangle getRectangle() {
		return new Rectangle(p);
	}
	
	@Override
	public IO sauvegarder(IO io) {
		return io.addBytePositif(getType().ordinal());
	}

	@Override
	public boolean estDecoupe() {
		return false;
	}

}
