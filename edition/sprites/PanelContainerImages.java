package sprites;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PanelContainerImages extends JPanel {
	private static final long serialVersionUID = 1L;
	private final Dimension taille;
	private BufferedImage[] images;


	public PanelContainerImages(Dimension taille, BufferedImage... images) {
		this.taille = taille;
		setImages(images);
	}

	public BufferedImage[] getImages() {
		return images;
	}

	public void setImages(BufferedImage... images) {
		this.images = images;
		build();
	}

	public void setImages(List<BufferedImage> images) {
		this.images = new BufferedImage[images.size()];
		for(int i=0 ; i<images.size() ; i++)
			this.images[i] = images.get(i);
		build();
	}

	private void build() {
		removeAll();
		for(final BufferedImage i : images)
			add(new JLabel(new ImageIcon(i.getScaledInstance(taille.width, taille.height, Image.SCALE_FAST))));
		validate();
		repaint();
	}

	public void vider() {
		removeAll();
		if(images != null) 
			for(int i=0 ; i<images.length ; i++) {
				images[i].flush();
				images[i] = null;
			}
		images = null;
	}


}
