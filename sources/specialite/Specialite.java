package specialite;

import interfaces.Fermable;
import interfaces.Lancable;

import java.awt.image.BufferedImage;
import java.util.Arrays;

import perso.AbstractPerso;
import ressources.Images;
import specialite.arme.Arme;
import specialite.competence.Competence;

public abstract class Specialite implements Lancable, Fermable {
    private final Competence[] competences;
    private final AbstractPerso source;


    public Specialite(AbstractPerso source, int xp) {
	this.source = source;
	competences = getCompetences(getType(), source, getNombre(xp));
    }

    public abstract TypeSpecialite getType();
    public abstract String getNomIcone();
    public abstract Arme getArme();

    public boolean aCompetence() {
	return competences.length > 0;
    }

    public Competence[] getCompetences() {
	return competences;
    }

    public AbstractPerso getSource() {
	return source;
    }

    @Override
    public boolean lancer() {
	return getArme().lancer();
    }

    @Override
    public boolean fermer() {
	return getArme().fermer();
    }

    public static Specialite get(AbstractPerso source, TypeSpecialite type, int xp) {
	switch(type) {
	case GUERRIER: return new SpecialiteGuerrier(source, xp);
	case ARCHER: return new SpecialiteArcher(source, xp);
	case INGENIEUR: return new SpecialiteIngenieur(source, xp);
	default: throw new IllegalArgumentException("Non implemente");
	}
    }

    public static Specialite get(AbstractPerso source, TypeSpecialite type) {
	return get(source, type, source.getXP(type));
    }

    public static Specialite[] getSpecialites(AbstractPerso source) {
	TypeSpecialite[] t = TypeSpecialite.values();
	Specialite[] s = new Specialite[t.length];
	for(int i=0 ; i<s.length ; i++)
	    s[i] = get(source, t[i]);
	return s;
    }

    public static Competence[] getCompetences(TypeSpecialite type, AbstractPerso source, int nombre) {
	Competence[] c = type.getCompetences(source);
	return Arrays.copyOf(c, Math.min(c.length, nombre));
    }

    public static int getNombre(int xp) {
	return Math.max(0, (int) Math.log10(xp));
    }

    public static int getXPPalier(int palier) {
	return (int) Math.pow(10, palier);
    }

    public BufferedImage getIcone() {
	return Images.get(Competence.PATH + getNomIcone() + ".png", true);
    }

}
