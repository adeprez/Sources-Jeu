package physique.forme;

import java.awt.Rectangle;

public interface Forme3D {
	public int getYGaucheHaut(Rectangle zone);
	public int getYGaucheBas(Rectangle zone);
	public int getYDroiteHaut(Rectangle zone);
	public int getYDroiteBas(Rectangle zone);
}
