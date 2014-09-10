package map.elements;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import physique.forme.Rect;
import vision.Camera;
import vision.Orientation;

public class ElementCiel extends Rect {
	private final BufferedImage image;
	private final float distance;

	
	public ElementCiel(int x, int y, int w, int h, float distance, BufferedImage image) {
		super(x, y, w, h, Orientation.DROITE);
		this.image = image;
		this.distance = distance;
	}
	
	public void dessiner(Camera c, Graphics2D g) {
		Rectangle zone = c.getZone(this, -distance);
		if(zone.intersects(c.getEcran().getBounds()))
			g.drawImage(image, zone.x, zone.y, zone.width, zone.height, null);
	}

	public float getDistance() {
		return distance;
	}
	
}
