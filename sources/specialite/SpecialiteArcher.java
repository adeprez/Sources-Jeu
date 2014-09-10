package specialite;

import perso.AbstractPerso;
import specialite.arme.Arc;
import specialite.arme.Arme;
import specialite.attaches.Carquois;

public class SpecialiteArcher extends Specialite {
	private final Carquois carquois;
	private final Arme arc;

	
	public SpecialiteArcher(AbstractPerso source, int xp) {
		super(source, xp);
		arc = new Arc(getSource().getAnimation());
		carquois = new Carquois(source.getAnimation());
	}
	
	@Override
	public boolean lancer() {
		return super.lancer() && carquois.lancer();
	}

	@Override
	public boolean fermer() {
		return super.fermer() && carquois.fermer();
	}
	
	@Override
	public String toString() {
		return "Archer";
	}
	
	@Override
	public TypeSpecialite getType() {
		return TypeSpecialite.ARCHER;
	}

	@Override
	public Arme getArme() {
		return arc;
	}

	@Override
	public String getNomIcone() {
		return "tir arc";
	}
	
}
