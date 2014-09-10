package ressources.sprites;

import interfaces.Sauvegardable;
import io.IO;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import ressources.Fichiers;
import ressources.Images;
import divers.Outil;


public class FeuilleSprite extends Sprite implements Sauvegardable {
	private final Dimension taille;
	private final String nom;
	private final int nombre;
	
	
	public FeuilleSprite(String nom, Dimension taille, BufferedImage... images) {
		super(images);
		this.nom = nom;
		this.taille = taille;
		nombre = images.length;
	}
	
	public FeuilleSprite(String nom) {
		this.nom = nom;
		IO io = Fichiers.lire(PATH + nom);
		nombre = io.nextShortInt();
		taille = new Dimension(io.nextShortInt(), io.nextShortInt());
		setImages(EnsembleSprite.getImages(nombre, Images.get(PATH + nom + ".png", false), taille));
	}
	
	public FeuilleSprite(String nom, int nombre, Dimension taille) {
		this.nom = nom;
		this.nombre = nombre;
		this.taille = taille;
		setImages(EnsembleSprite.getImages(nombre, Images.get(PATH + nom + ".png", false), taille));
	}

	public Dimension getDimension() {
		return taille;
	}

	public void setImage(int id, BufferedImage image) {
		if(id >= nombre())
			setImages(Arrays.copyOf(getImages(), nombre() + (id + 1 - nombre())));
		getImages()[id] = image;
	}

	public String getNom() {
		return nom;
	}
	
	public void enregistrer() {
		Outil.save(this, PATH + nom);
	}
	
	@Override
	public IO sauvegarder(IO io) {
		return io.addShort(nombre).addShort(taille.width).addShort(taille.height);
	}

}
