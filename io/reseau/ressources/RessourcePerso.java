package reseau.ressources;

import io.IO;
import map.Map;
import perso.Perso;
import perso.PersoIO;
import reseau.paquets.jeu.PaquetPosition;
import exceptions.HorsLimiteException;

public class RessourcePerso extends RessourceReseau<Perso> {
	private Perso perso;

	
	public RessourcePerso(int id, Perso perso) {
		super(id);
		this.perso = perso;
	}

	public RessourcePerso(IO io) {
		super(io);
		perso = PersoIO.get(io);
	}
	
	public RessourcePerso(IO io, Map map) {
		this(io);
		perso.setMap(map);
		if(io.aByte()) try {
			PaquetPosition.effet(io, perso);
		} catch(HorsLimiteException e) {
			e.printStackTrace();
		}
	}
	
	public Perso getPerso() {
		return perso;
	}

	public void setPerso(Perso perso) {
		this.perso = perso;
	}

	@Override
	public TypeRessource getType() {
		return TypeRessource.PERSO;
	}

	@Override
	public void ecrireDonnees(IO io) {
		perso.ecrireDonnees(io);
		if(perso.estDansMap())
			PaquetPosition.ecrire(io, perso);
	}

	@Override
	public Perso getRessource() {
		return getPerso();
	}

	@Override
	public void affecteRessource(Perso ressource) {
		setPerso(ressource);
	}

}
