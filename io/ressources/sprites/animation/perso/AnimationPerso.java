package ressources.sprites.animation.perso;

import interfaces.Localise;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import ressources.sprites.SpritePerso;
import ressources.sprites.animation.Animation;
import ressources.sprites.animation.Membre;
import divers.Couple;
import divers.Outil;

public class AnimationPerso extends Animation {
    public static final int AVANT_BRAS_ARR = 0, MAIN_ARR = 1, BRAS_ARR = 2, TIBIA_ARR = 3, JAMBE_ARR = 4, PIED_ARR = 5,
	    CORPS = 6, TETE = 7, TIBIA_AV = 8, JAMBE_AV = 9, PIED_AV = 10, MAIN_AVANT = 11, AVANT_BRAS_AV = 12, BRAS_AV = 13;
    private final JambePerso jambeAvant, jambeArriere;
    private final BrasPerso brasAvant, brasArriere;
    private final BufferedImage image;
    private final Membre corps, tete;
    private final SpritePerso sprite;
    private final Membre[] membres;


    public AnimationPerso(BufferedImage image) {
	this.image = image;
	sprite = new SpritePerso(image);
	corps = new Membre(this, sprite.getCorps(), 100, 190, 50, 60, 0);
	tete = new Membre(corps, sprite.getTete(), 100, 100, 40, 80, 0);
	brasAvant = new BrasPerso(corps, sprite, true);
	brasArriere = new BrasPerso(corps, sprite, false);
	jambeAvant = new JambePerso(corps, sprite, true);
	jambeArriere = new JambePerso(corps, sprite, false);
	membres = new Membre[] {
		brasArriere.getAvantBras(), brasArriere.getMain(), brasArriere,
		jambeArriere.getTibia(), jambeArriere, jambeArriere.getPied(),
		corps, tete,
		jambeAvant.getTibia(), jambeAvant, jambeAvant.getPied(),
		brasAvant.getMain(), brasAvant.getAvantBras(), brasAvant
	};
	corps.setNom("Corps");
	tete.setNom("Tete");
    }

    public Membre getCorpsPerso() {
	return corps;
    }

    public Membre getTetePerso() {
	return tete;
    }

    public BrasPerso getBras(boolean avant) {
	return avant ? brasAvant : brasArriere;
    }

    public JambePerso getJambe(boolean avant) {
	return avant ? jambeAvant : jambeArriere;
    }

    @Override
    public int getY(Rectangle zone) {
	return (int) (zone.y + zone.height - zone.width * 2.5 - Outil.getValeur(getDecalageY(), zone.width));
    }

    @Override
    public Membre[] getMembres() {
	return membres;
    }

    @Override
    public BufferedImage getImage() {
	return image;
    }

    @Override
    public AnimationPerso dupliquer() {
	return new AnimationPerso(image);
    }

    @Override
    public Couple<Membre, Integer> getMembre(Localise source, Localise ancrable) {
	int diff = ancrable.getY() - source.getY(), pH = Outil.getPourcentage(diff, source.getHauteur()), y;
	Membre m;
	if(pH < 8) {
	    m = Outil.r().nextBoolean() ? jambeArriere.getPied() : jambeAvant.getPied();
	    y = Outil.getPourcentage(diff, 8);
	} else if(pH < 20) {
	    m = Outil.r().nextBoolean() ? jambeArriere.getTibia() : jambeAvant.getTibia();
	    y = Outil.getPourcentage(diff - 10, 20);
	} else if(pH < 35) {
	    m = Outil.r().nextBoolean() ? jambeArriere.getTibia() : jambeAvant.getTibia();
	    y = Outil.getPourcentage(diff - 35, 20);
	} else if(pH < 80) {
	    m = corps;
	    y = Outil.getPourcentage(diff - 60, 60);
	} else {
	    m = tete;
	    y = Outil.getPourcentage(pH - 75, 25);
	}
	return new Couple<Membre, Integer>(m, 100 - Outil.entre(y, 10, 80));
    }

}
