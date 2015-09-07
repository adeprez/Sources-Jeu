package perso;

import java.util.Arrays;

import ressources.Images;
import ressources.sprites.animation.perso.AnimationPerso;
import specialite.Specialite;
import specialite.TypeSpecialite;
import divers.Outil;

public class Bot extends Perso {

	
	public Bot(int palier) {
		super("Ordinateur", 0, getXP(palier), creerInfos(), creerCaract(), getAnim());
		setSpecialite(Outil.r().nextInt(getSpecialites().length));
	}
	
	public static int[] getXP(int palier) {
		int[] xp = new int[TypeSpecialite.values().length];
		Arrays.fill(xp, Specialite.getXPPalier(palier));
		return xp;
	}
	
	public static Caracteristiques creerCaract() {
		return new Caracteristiques(100, 50, 1000);
	}
	
	public static InformationsPerso creerInfos() {
		Sexe s = Sexe.random();
		return new InformationsPerso(40, creerNom(s), s);
	}

	public static String creerNom(Sexe s) {
		return "Bot " + s.getNom();
	}
	
	public static AnimationPerso getAnim() {
		return new AnimationPerso(Images.get("anim/sprite.png", true));
	}

}
