package specialite;

import perso.AbstractPerso;
import specialite.arme.Arme;
import specialite.arme.Epee;
import specialite.attaches.Bouclier;
import specialite.attaches.MembreAttachable;

public class SpecialiteGuerrier extends Specialite {
	private final MembreAttachable bouclier;
	private final Arme epee;

	
	public SpecialiteGuerrier(AbstractPerso source, int xp) {
		super(source, xp);
		epee = new Epee(getSource().getAnimation());
		bouclier = new Bouclier(getSource().getAnimation());
	}
	
	@Override
	public String toString() {
		return "Guerrier";
	}
	
	@Override
	public boolean lancer() {
		return super.lancer() && bouclier.lancer();
	}

	@Override
	public boolean fermer() {
		return super.fermer() && bouclier.fermer();
	}

	@Override
	public TypeSpecialite getType() {
		return TypeSpecialite.GUERRIER;
	}

	@Override
	public Arme getArme() {
		return epee;
	}

	@Override
	public String getNomIcone() {
		return "coup epee";
	}
	
}
