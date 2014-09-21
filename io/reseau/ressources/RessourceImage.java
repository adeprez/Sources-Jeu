package reseau.ressources;

import interfaces.ContaineurImageOp;
import io.IO;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import ressources.RessourcesLoader;

public class RessourceImage extends RessourceReseau<BufferedImage> implements ContaineurImageOp {
    private Map<Integer, BufferedImage> variantes;
    private BufferedImage image;
    private IO data;


    public RessourceImage(int id, BufferedImage image) {
	super(id);
	this.image = image;
    }

    public RessourceImage(IO io) {
	super(io);
	image = getImage(io);
    }

    public void setImage(BufferedImage image) {
	this.image = image;
	data = null;
	variantes = null;
    }

    @Override
    public BufferedImage getImage(int teinte, int opacite) {
	if(variantes == null)
	    variantes = new HashMap<Integer, BufferedImage>();
	int clef = teinte * 101 + opacite;
	BufferedImage img = variantes.get(teinte * 100 + opacite);
	if(img == null) {
	    img = getOp(image, teinte, opacite);
	    variantes.put(clef, img);
	}
	return img;
    }

    @Override
    public BufferedImage getImage() {
	return image;
    }

    @Override
    public TypeRessource getType() {
	return TypeRessource.IMAGE;
    }

    @Override
    public void ecrireDonnees(IO io) {
	if(data == null)
	    data = getIO(image);
	io.add(data);
    }

    @Override
    public BufferedImage getRessource() {
	return getImage();
    }

    @Override
    public void affecteRessource(BufferedImage ressource) {
	setImage(ressource);
    }

    public static IO getIO(BufferedImage img) {
	return ecrire(new IO((2 + img.getWidth() * img.getHeight()) * 4), img);
    }

    public static BufferedImage getOp(BufferedImage image, int teinte, int opacite) {
	BufferedImage img = RessourcesLoader.creerImage(image.getWidth(), image.getHeight());
	Graphics2D g = img.createGraphics();
	g.drawImage(image, 0, 0, null);
	g.setColor(new Color(teinte, teinte, teinte, opacite));
	g.setComposite(AlphaComposite.SrcAtop);
	g.fillRect(0, 0, img.getWidth(), img.getHeight());
	g.dispose();
	return img;
    }

    public static IO ecrire(IO io, BufferedImage img) {
	io.addShort(img.getWidth()).addShort(img.getHeight());
	for(int x=0 ; x<img.getWidth() ; x++)
	    for(int y=0 ; y<img.getHeight() ; y++)
		io.add(img.getRGB(x, y));
	return io;
    }

    public static BufferedImage getImage(IO io) {
	BufferedImage img = new BufferedImage(io.nextShortInt(), io.nextShortInt(), BufferedImage.TYPE_INT_ARGB);
	for(int x=0 ; x<img.getWidth() ; x++)
	    for(int y=0 ; y<img.getHeight() ; y++)
		img.setRGB(x, y, io.nextInt());
	return img;
    }

}
