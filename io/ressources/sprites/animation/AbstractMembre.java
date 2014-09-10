package ressources.sprites.animation;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import divers.Outil;

public abstract class AbstractMembre implements Positionable {
	private final Rectangle forme;
	private final int py;


	public AbstractMembre(int l, int h, int x, int y, int py) {
		forme = new Rectangle(x, y, l, h);
		this.py = py;
	}
	
	public abstract int getX(Rectangle zone, boolean droite);
	public abstract int getY(Rectangle zone);
	public abstract float getAngle(boolean droite);
	public abstract void dessiner(Graphics2D g, Rectangle zone, boolean droite);
	public abstract int getDecalage(AbstractMembre enfant, Rectangle zone, double angle);

	@Override
	public int getPrctX() {
		return forme.x;
	}

	@Override
	public int getPrctY() {
		return forme.y;
	}

	@Override
	public int getPrctYParent() {
		return py;
	}

	@Override
	public int getPrctLargeur() {
		return forme.width;
	}

	@Override
	public int getPrctHauteur() {
		return forme.height;
	}

	public int getLargeur(Rectangle zone) {
		return val(getPrctLargeur(), zone.width);
	}

	public int getHauteur(Rectangle zone) {
		return val(getPrctHauteur(), zone.width);
	}

	public int getDecalageX(AbstractMembre enfant, Rectangle zone, boolean droite) {
		return getDecalage(enfant, zone, Math.sin(getAngle(droite)));
	}

	public int getDecalageY(AbstractMembre enfant, Rectangle zone) {
		return getDecalage(enfant, zone, Math.cos(getAngle(true)));
	}
	
	protected static int val(int v, int max) {
		return Outil.getValeur(v, max);
	}

}
