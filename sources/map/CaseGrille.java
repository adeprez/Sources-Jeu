package map;

import interfaces.Localise;

import java.awt.Point;

import physique.Collision;
import exceptions.HorsLimiteException;

public class CaseGrille implements Localise {
	private final Point pos;
	
	
	public CaseGrille(int x, int y) {
		pos = new Point(x, y);
	}

	@Override
	public int getX() {
		return pos.x;
	}

	@Override
	public int getY() {
		return pos.y;
	}

	@Override
	public Collision setX(int x) {
		pos.x = x;
		return null;
	}

	@Override
	public Collision setY(int y) {
		pos.y = y;
		return null;
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
	public Collision setPos(int x, int y) throws HorsLimiteException {
		setX(x);
		setY(y);
		return null;
	}

	@Override
	public Collision setHauteur(int hauteur) {
		return null;
	}

	@Override
	public Collision setLargeur(int largeur) {
		return null;
	}

}
