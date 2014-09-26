package ressources.sprites;

import io.IO;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import ressources.Enregistrable;
import ressources.Fichiers;
import ressources.Images;

public class EnsembleSprite extends Enregistrable {
    private final Dimension taille;
    private final int nombre;
    private final int[] bornes;
    private Sprite[] sprites;
    private int idAnim;


    public EnsembleSprite(String nom) {
	this(nom, "png");
    }

    public EnsembleSprite(String nom, String extension) {
	super(Sprite.PATH + nom);
	IO io = Fichiers.lire(Sprite.PATH + nom);
	taille = new Dimension(io.nextShortInt(), io.nextShortInt());
	bornes = new int[io.nextShortInt()];
	for(int i=0 ; i<bornes.length ; i++)
	    bornes[i] = io.nextShortInt();
	nombre = bornes[bornes.length - 1];
	construire(Images.get(Sprite.PATH + nom + '.' + extension, false));
    }

    public EnsembleSprite(String nom, BufferedImage image, Dimension taille, int... bornes) {
	super(nom);
	this.taille = taille;
	this.bornes = bornes;
	nombre = bornes[bornes.length - 1];
	construire(image);
    }

    public void setAnim(int idAnim) {
	this.idAnim = idAnim;
    }

    public int getIDAnim() {
	return idAnim;
    }

    public Sprite getSprite() {
	return sprites[idAnim];
    }

    private void construire(BufferedImage image) {
	BufferedImage[] images = getImages(nombre, image, taille);
	sprites = new Sprite[bornes.length];
	int i=0;
	for(int index=0 ; index<bornes.length ; index++)
	    if(index + 1 == bornes[i])
		sprites[i++] = new Sprite(Arrays.copyOfRange(images, index < 0 ? 0 : bornes[index - 1], bornes[index]));
    }

    @Override
    public IO sauvegarder(IO io) {
	return io.addShort(taille.width).addShort(taille.height).addShort(sprites.length).addShortsPositif(bornes);
    }

    public static BufferedImage[] getImages(int nombre, BufferedImage image, Dimension taille) {
	BufferedImage[] images = new BufferedImage[nombre];
	int i = 0;
	for(int y=0 ; y<image.getHeight() ; y+=taille.height)
	    for(int x=0; x<image.getWidth() && i<nombre ; x+=taille.width)
		images[i++] = image.getSubimage(x, y, taille.width, taille.height);
	return images;
    }

}
