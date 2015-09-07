package specialite;

import perso.AbstractPerso;
import specialite.arme.Arme;
import specialite.arme.Mortier;

public class SpecialiteIngenieur extends Specialite {
    private final Arme epee;


    public SpecialiteIngenieur(AbstractPerso source, int xp) {
	super(source, xp);
	epee = new Mortier(getSource().getAnimation());
    }

    @Override
    public String toString() {
	return "Ingenieur";
    }

    @Override
    public TypeSpecialite getType() {
	return TypeSpecialite.INGENIEUR;
    }

    @Override
    public Arme getArme() {
	return epee;
    }

    @Override
    public String getNomIcone() {
	return "charge";
    }

}
