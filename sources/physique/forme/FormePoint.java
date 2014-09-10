package physique.forme;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;

import physique.Collision;

public class FormePoint extends Forme {
	private final Point origine;
	
	
	public FormePoint(Point origine) {
		this.origine = origine;
	}
	
	public FormePoint(int x, int y) {
		this(new Point(x, y));
	}
	
	@Override
	public int getX() {
		return origine.x;
	}

	@Override
	public int getY() {
		return origine.y;
	}

	@Override
	public int getLargeur() {
		return 1;
	}

	@Override
	public int getHauteur() {
		return 1;
	}

	@Override
	public TypeForme getType() {
		return TypeForme.POINT;
	}

	@Override
	public Shape getDecoupe(Rectangle forme) {
		return forme;
	}

	@Override
	public boolean intersection(Forme autre) {
		return autre.intersection(this);
	}

	@Override
	public Rectangle getRectangle() {
		return new Rectangle(getX(), getY(), getLargeur(), getHauteur());
	}

	@Override
	public boolean estDecoupe() {
		return false;
	}

	@Override
	public void setXForme(int x) {
		origine.x = x;
	}

	@Override
	public void setYForme(int y) {
		origine.y = y;
	}

	@Override
	protected FormePoint creerCopie() {
		return new FormePoint(getX(), getY());
	}

	@Override
	public Collision setLargeur(int largeur) {
		return null;
	}

	@Override
	public Collision setHauteur(int hauteur) {
		return null;
	}

}
