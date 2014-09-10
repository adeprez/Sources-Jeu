package reseau.ressources;

import io.IO;
import map.Map;

public class RessourceMap extends RessourceReseau<Map> {
	private Map map;

	
	public RessourceMap(int id, Map map) {
		super(id);
		this.map = map;
	}
	
	public RessourceMap(RessourcesReseau r, IO io) {
		super(io);
		map = new Map(r, r instanceof RessourcesServeur ? ((RessourcesServeur) r).getServeur() : null, io);
		map.setLargeurExtensible(false);
	}
	
	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	@Override
	public TypeRessource getType() {
		return TypeRessource.MAP;
	}

	@Override
	public void ecrireDonnees(IO io) {
		map.sauvegarder(io);
	}

	@Override
	public Map getRessource() {
		return getMap();
	}

	@Override
	public void affecteRessource(Map ressource) {
		setMap(ressource);
	}

}
