package physique.forme;

import java.awt.Dimension;

import vision.Orientation;

public class CorpsPerso extends Rect {


	public CorpsPerso(Dimension taille) {
		super(taille.width, taille.height, Orientation.DROITE);
	}

	@Override
	public CorpsPerso dupliquer() {
		return new CorpsPerso(getDimension());
	}

}
