package ressources.sprites.animation.perso;

import ressources.sprites.SpritePerso;
import ressources.sprites.animation.Membre;

public class BrasPerso extends Membre {
    private final Membre avantBras, main;
    private final boolean avant;


    public BrasPerso(Membre corps, SpritePerso sprite, boolean avant) {
	super(corps, sprite.getBras(avant), 50, 80, 50, 20, 30);
	avantBras = new Membre(this, sprite.getAvantBras(avant), 50, 70, 45, 15, 95);
	main = new Membre(avantBras, sprite.getMain(avant), 45, 40, 50, 10, 90);
	this.avant = avant;
	String s = avant ? "avant" : "arriere";
	setNom("Bras " + s);
	avantBras.setNom("Avant-bras " + s);
	main.setNom("Main " + s);
    }

    public Membre getAvantBras() {
	return avantBras;
    }

    public Membre getMain() {
	return main;
    }

    public boolean estAvant() {
	return avant;
    }

}
