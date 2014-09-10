package bordures;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.image.BufferedImage;

import javax.swing.border.Border;

import ressources.Images;

public class BordureImage implements Border {
	private static final String PATH = "bordures/";
	private final BufferedImage image;

	
	public BordureImage(String nom) {
		image = Images.get(PATH + nom, true);
	}
	
	@Override
	public Insets getBorderInsets(Component c) {
		return new Insets(3, 3, 3, 3);
	}

	@Override
	public boolean isBorderOpaque() {
		return false;
	}

	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		g.drawImage(image, x, y, width, height, null);
	}

}
