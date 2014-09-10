package composants.panel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import ressources.Images;

public class PanelDeuxImages extends PanelImage {
	private static final long serialVersionUID = 1L;
	private final BufferedImage image;

	
	public PanelDeuxImages(BufferedImage img1, BufferedImage img2) {
		super(img1);
		image = img2;
	}
	
	public PanelDeuxImages(String img1, String img2) {
		this(Images.get(img1, false), Images.get(img2, false));
	}

	public PanelImage tailleImage2() {
		if(image != null)
			setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
		return this;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(image!=null) {
			setZone(image);
			Rectangle bounds = getZone();
			g.drawImage(image, bounds.x, bounds.y, bounds.width, bounds.height, null);
		}
	}
	
}
