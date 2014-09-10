package interfaces;

import java.awt.image.BufferedImage;


public interface ContaineurImageOp extends ContaineurImage {
	public BufferedImage getImage(int teinte, int opacite);
}
