package ressources.sprites.animation;

import java.awt.image.BufferedImage;

public interface Positionable {
	public BufferedImage getImage();
	public int getPrctLargeur();
	public int getPrctHauteur();
	public int getPrctX();
	public int getPrctY();
	public int getPrctYParent();
}
