package reseau.ressources;

import io.IO;
import reseau.objets.InfoPartie;

public class RessourceJeu extends RessourceReseau<InfoPartie> {
	private InfoPartie infos;

	
	public RessourceJeu(int id, InfoPartie infos) {
		super(id);
		this.infos = infos;
	}
	
	public RessourceJeu(IO io) {
		super(io);
		infos = new InfoPartie(io);
	}
	
	public InfoPartie getInfos() {
		return infos;
	}

	public void setInfos(InfoPartie infos) {
		this.infos = infos;
	}

	@Override
	public TypeRessource getType() {
		return TypeRessource.CONFIG_JEU;
	}

	@Override
	public void ecrireDonnees(IO io) {
		infos.sauvegarder(io);
	}

	@Override
	public InfoPartie getRessource() {
		return infos;
	}

	@Override
	public void affecteRessource(InfoPartie ressource) {
		infos = ressource;
	}
	
	@Override
	public String toString() {
		return infos == null ? "null" : infos.toString();
	}

}
