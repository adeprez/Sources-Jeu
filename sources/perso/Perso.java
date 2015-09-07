package perso;

import io.IO;
import ressources.sprites.animation.perso.AnimationPerso;
import vision.RevelateurObjets;

public class Perso extends PersoIO {


    public Perso(String compte, IO xp, IO infos, IO caract, IO anim) {
	super(compte, xp, infos, caract, anim);
    }

    public Perso(String compte, String nom) {
	super(compte, nom);
    }

    public Perso(String compte, int xp, int[] xps, InformationsPerso infos, Caracteristiques caract, AnimationPerso anim) {
	super(compte, xp, xps, infos, caract, anim);
    }

    public void setPrincipal() {
	setChangeCaseListener(new RevelateurObjets(getMap()));
    }

}
