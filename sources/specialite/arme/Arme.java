package specialite.arme;

import interfaces.Fermable;
import interfaces.Lancable;

import java.awt.image.BufferedImage;

import perso.Perso;
import physique.actions.vivant.ActionVivant;
import ressources.Images;
import ressources.sprites.animation.Membre;

public abstract class Arme extends Membre implements Lancable, Fermable {
	private final static String PATH = "armes/";

	
	public Arme(Membre parent, BufferedImage image, int l, int h, int x, int y, int py) {
		super(parent, image, l, h, x, y, py);
	}

	public static BufferedImage getImage(String nom) {
		return Images.get(PATH + nom + ".png", true);
	}

	public abstract ActionVivant<?> getAction(Perso perso);
	public abstract int getDegats();

	@Override
	public boolean fermer() {
		((Membre) getParent()).removeMembre(this);
		return true;
	}

	@Override
	public boolean lancer() {
		((Membre) getParent()).addMembre(this);
		return true;
	}
	
}
