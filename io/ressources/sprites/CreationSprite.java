package ressources.sprites;

import interfaces.TacheRunnable;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import divers.Outil;

public class CreationSprite implements TacheRunnable {
	private final BufferedImage[] images;
	private final Dimension d;
	private BufferedImage image;
	private int i;


	public CreationSprite(Dimension d, BufferedImage... images) {
		this.d = d;
		this.images = images;
	}
	
	public BufferedImage getImage() {
		if(image == null)
			executer();
		return image;
	}

	@Override
	public void executer() {
		int colonnes = (int) Math.max(1, Math.sqrt(images.length));
		int lignes = Math.max(1, (images.length/colonnes));
		while(lignes * colonnes < images.length)
			lignes ++;
		image = new BufferedImage(colonnes * d.width, lignes*d.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		for(int y=0 ; y<image.getHeight() ; y+=d.height)
			for(int x=0 ; x<image.getWidth() && i<images.length ; x+=d.width, i++) {
				g.setClip(x, y, d.width, d.height);
				g.drawImage(images[i], x, y, d.width, d.height, null);
			}
	}

	@Override
	public int getAvancement() {
		return Outil.getPourcentage(i + 1, images.length);
	}

}
