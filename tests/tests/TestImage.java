package tests;
import static org.junit.Assert.assertEquals;
import io.IO;

import java.awt.image.BufferedImage;

import org.junit.Test;

import reseau.ressources.RessourceImage;
import reseau.ressources.RessourcesReseau;


@SuppressWarnings("static-method")
public class TestImage {


    @Test
    public void testAlgo() {
	BufferedImage img;
	img = new BufferedImage(32, 64, BufferedImage.TYPE_INT_ARGB);
	img.createGraphics().fillRect(10, 20, 12, 13);
	BufferedImage copy = RessourceImage.getImage(RessourceImage.getIO(img));
	assertEquals(img.getWidth(), copy.getWidth());
	assertEquals(img.getHeight(), copy.getHeight());
	for(int x=0; x<img.getWidth() ; x++)
	    for(int y=0 ; y<img.getHeight() ; y++)
		assertEquals(img.getRGB(x, y), copy.getRGB(x, y));
    }

    @Test
    public void testIO() {
	RessourceImage img = new RessourceImage(3, new BufferedImage(3, 5, BufferedImage.TYPE_INT_ARGB));
	RessourceImage copy = (RessourceImage) new RessourcesReseau().lire(img.sauvegarder(new IO()));
	assertEquals(img.getImage().getWidth(), copy.getImage().getWidth());
	assertEquals(img.getImage().getHeight(), copy.getImage().getHeight());
	for(int x=0; x<img.getImage().getWidth() ; x++)
	    for(int y=0 ; y<img.getImage().getHeight() ; y++)
		assertEquals(img.getImage().getRGB(x, y), copy.getImage().getRGB(x, y));
    }

}
