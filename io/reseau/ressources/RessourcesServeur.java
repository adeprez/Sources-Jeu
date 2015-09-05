package reseau.ressources;

import interfaces.FiltreEnvoi;
import map.Map;
import partie.modeJeu.Jeu;
import reseau.objets.InfoPartie;
import reseau.paquets.Paquet;
import reseau.paquets.TypePaquet;
import reseau.serveur.Serveur;
import reseau.serveur.filtreEnvoi.DefaultFiltreEnvoi;

public class RessourcesServeur extends RessourcesReseau {
    private final Serveur serveur;
    private Map map;
    private Jeu jeu;


    public RessourcesServeur(Serveur serveur) {
	this.serveur = serveur;
    }

    public Serveur getServeur() {
	return serveur;
    }

    public RessourceMap setMap(Map map, int extensionLaterale, int extensionBas) {
	this.map = map;
	map.etendre(extensionLaterale, extensionBas);
	RessourceMap r = new RessourceMap(0, map);
	putRessource(r);
	return r;
    }

    public void setJeu(Jeu jeu, int temps) {
	this.jeu = jeu;
	putRessource(new RessourceJeu(0, new InfoPartie(jeu.getType(), temps)));
    }

    public void configurer(Jeu jeu, int temps, Map map, int extensionLaterale, int extensionBas) {
	map.setServeur(serveur);
	jeu.prepareMap(map);
	setJeu(jeu, temps);
	setMap(map, extensionLaterale, extensionBas);
    }

    public Jeu getJeu() {
	return jeu;
    }

    public void putRessource(RessourceReseau<?> ressource, FiltreEnvoi filtre) {
	super.putRessource(ressource);
	serveur.envoyerFiltre(new Paquet(TypePaquet.ADD_RESSOURCE, ressource), filtre);
    }

    @Override
    public Map getMap() {
	return map;
    }

    @Override
    public boolean estServeur() {
	return true;
    }

    @Override
    public void removeRessource(RessourceReseau<?> ressource) {
	super.removeRessource(ressource);
	serveur.envoyerTous(new Paquet(TypePaquet.REMOVE_RESSOURCE, ressource.getType().getID(), ressource.getID()));
    }

    @Override
    public <E> void putRessource(RessourceReseau<E> ressource) {
	putRessource(ressource, new DefaultFiltreEnvoi());
    }

    @Override
    public String toString() {
	return "(Serveur) " + super.toString();
    }

}
