package specialite.attaches;

import interfaces.Fermable;
import interfaces.Lancable;

import java.awt.image.BufferedImage;

import ressources.sprites.animation.Membre;

public class MembreAttachable extends Membre implements Lancable, Fermable {
	private final Membre parent;

	
	public MembreAttachable(Membre parent, BufferedImage image, int l, int h, int x, int y, int py) {
		super(parent, image, l, h, x, y, py);
		this.parent = parent;
	}
	
	@Override
	public Membre getParent() {
		return parent;
	}
	
	@Override
	public boolean fermer() {
		parent.removeMembre(this);
		return true;
	}

	@Override
	public boolean lancer() {
		parent.addMembre(this);
		return true;
	}

}
