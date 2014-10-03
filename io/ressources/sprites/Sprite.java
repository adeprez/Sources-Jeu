package ressources.sprites;

import interfaces.ContaineurImageOp;
import interfaces.ContaineurImagesOp;
import interfaces.Dessinable;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import javax.imageio.ImageIO;

import ressources.Images;
import ressources.RessourcesLoader;

import composants.styles.EcranAttente;

import divers.Listenable;
import divers.Outil;

public class Sprite extends Listenable implements Dessinable, ContaineurImagesOp, ContaineurImageOp {
    public static final String PATH = "sprites/";
    private BufferedImage[] images;
    private boolean moins;
    private int index;


    public Sprite() {}

    public Sprite(BufferedImage... images) {
	this();
	setImages(images);
    }

    public void setImages(BufferedImage... images) {
	this.images = images;
    }

    public void addImage(BufferedImage image) {
	setImages(Arrays.copyOf(images, images.length + 1));
	images[images.length - 1] = image;
    }

    public void removeImage(int id) {
	BufferedImage[] img = new BufferedImage[images.length - 1];
	for(int i=0 ; i<images.length ; i++)
	    if(i != id)
		img[i] = images[i - (i < id ? 0 : 1)];
	setImages(img);
    }

    public synchronized void boucle() {
	if(moins) {
	    index --;
	    moins = index != 0;
	} else {
	    index ++;
	    moins = index >= images.length - 1;
	}
    }

    public synchronized boolean suivant() {
	return index ++ < images.length;
    }

    public void setIndex(int index) {
	this.index = index;
    }

    public int nombre() {
	return images.length;
    }

    public BufferedImage[] getImages() {
	return images;
    }

    public int getIndex() {
	return index;
    }

    @Override
    public BufferedImage getImage(int teinte, int opacite) {
	return getImage();
    }

    @Override
    public ContaineurImageOp getImageOp(int id) {
	index = id;
	return this;
    }

    @Override
    public BufferedImage getImage() {
	return getImage(getIndex());
    }

    @Override
    public BufferedImage getImage(int index) {
	try {
	    return images[index];
	} catch(IndexOutOfBoundsException err) {
	    return new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_BINARY);
	}
    }

    @Override
    public void dessiner(Graphics2D g, Rectangle zone, int equipe) {
	g.drawImage(getImage(), zone.x, zone.y, zone.width, zone.height, null);
    }

    @Override
    public void predessiner(Graphics2D g, Rectangle zone, int equipe) {}

    @Override
    public void surdessiner(Graphics2D g, Rectangle zone, int equipe) {}

    public static boolean creer(FeuilleSprite sprite) {
	return creerSprite(sprite.getNom(), sprite.getDimension(), sprite.getImages());
    }

    public static boolean creerSprite(String nom, Dimension d, BufferedImage... images) {
	try {
	    BufferedImage img = creer(d, images);
	    ImageIO.write(img, "png", RessourcesLoader.getFichier(Images.PATH + Sprite.PATH + nom + ".png", false));
	    new FeuilleSprite(nom, d, images).enregistrer();
	    img.flush();
	    img = null;
	    return true;
	} catch(Exception e) {
	    Outil.erreur("Erreur lors de la creation du sprite : " + e.getMessage());
	    e.printStackTrace();
	    return false;
	}
    }

    public static BufferedImage creer(final Dimension d, final BufferedImage... images) {
	CreationSprite c = new CreationSprite(d, images);
	new EcranAttente("Creation du sprite...", c);
	return c.getImage();
    }

}
