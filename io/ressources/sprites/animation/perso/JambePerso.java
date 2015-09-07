package ressources.sprites.animation.perso;

import ressources.sprites.SpritePerso;
import ressources.sprites.animation.Membre;

public class JambePerso extends Membre {
	private final Membre tibia, pied;
	private final boolean avant;

	public JambePerso(Membre corps, SpritePerso sprite, boolean avant) {
		super(corps, sprite.getCuisse(avant), 80, 110, 60, 20, 95);
		tibia = new Membre(this, sprite.getJambe(avant), 70, 110, 60, 10, 100);
		pied = new Membre(tibia, sprite.getPied(avant), 70, 45, 45, 20, 80);
		this.avant = avant;
		String s = avant ? "avant" : "arriere";
		setNom("Jambe " + s);
		tibia.setNom("Tibia " + s);
		pied.setNom("Pied " + s);
	}

	public Membre getTibia() {
		return tibia;
	}

	public Membre getPied() {
		return pied;
	}

	public boolean estAvant() {
		return avant;
	}
	
}
