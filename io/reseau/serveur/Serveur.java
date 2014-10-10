package reseau.serveur;

import io.IO;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import map.Map;
import map.objets.Objet;
import partie.PartieServeur;
import partie.modeJeu.Jeu;
import perso.Bot;
import reseau.AbstractClient;
import reseau.listeners.FinChargementListener;
import reseau.objets.InfoServeur;
import reseau.paquets.Paquet;
import reseau.paquets.TypePaquet;
import reseau.paquets.session.PaquetInfoServeur;
import reseau.ressources.RessourceImageObjet;
import reseau.ressources.RessourcePerso;
import reseau.ressources.RessourcesServeur;
import reseau.ressources.TypeRessource;
import temps.CompteARebours;
import base.IconeTaches;

public class Serveur extends AbstractServeur<ClientServeur> implements FinChargementListener {
    public static final int DEFAULT_PORT = 9876;
    private static Serveur instance;
    private final RessourcesServeur ressources;
    private final CompteARebours cpt;
    private final InfoServeur infos;
    private final List<Bot> bots;
    private PartieServeur partie;


    public static Serveur getInstance() {
	return instance;
    }

    public Serveur(String createur) throws IOException {
	this(DEFAULT_PORT, new InfoServeur(createur));
    }

    public Serveur(InfoServeur infos) throws IOException {
	this(DEFAULT_PORT, infos);
    }

    public Serveur(int port, InfoServeur infos) throws IOException {
	super(port);
	this.infos = infos;
	bots = new ArrayList<Bot>();
	ressources = new RessourcesServeur(this);
	cpt = new CompteARebours(infos.getDelai());
	cpt.addHorlogeListener(infos);
	cpt.addFinChargementListener(this);
	instance = this;
    }

    public InfoServeur getInfosServeur() {
	return infos;
    }

    public void configurer(Map map) {
	for(final List<Objet> lo : map.getObjets())
	    for(final Objet o : lo) {
		if(o.getImage() != null && !ressources.aRessource(TypeRessource.IMAGE_OBJET, o.getID()))
		    ressources.putRessource(new RessourceImageObjet(o));
		if(o.aFond() && !ressources.aRessource(TypeRessource.IMAGE_OBJET, o.getFond()))
		    ressources.putRessource(new RessourceImageObjet(o.getFond(), o.getImageFond()));
	    }
	ressources.setJeu(Jeu.getJeu(infos.getTypeJeu(), this, infos.getScoreVictoire()), infos.getTemps());
	ressources.setMap(map);
	partie = new PartieServeur(this);
	ajoutBots(3);
    }

    public void ajoutBots(int nombre) {
	for(int i=0 ; i<nombre ; i++)
	    ajoutBot();
    }

    public void ajoutBot() {
	Bot b = new Bot(3);
	int id = getMaxClients() - bots.size();
	ressources.putRessource(new RessourcePerso(id, b));
	getPartie().spawn(id);
	bots.add(b);
    }

    public PartieServeur getPartie() {
	return partie;
    }

    public CompteARebours getHorloge() {
	return cpt;
    }

    public void recalculeTempsLancement() {
	if(!partie.estLancee()) {
	    Collection<ClientServeur> clients = getClients();
	    int prets = 0;
	    for(final ClientServeur c : clients)
		if(c.estPret())
		    prets++;
	    if(prets == clients.size())
		finChargement();
	}
    }

    @Override
    public boolean fermer() {
	infos.setEtat(InfoServeur.ETAT_OFF);
	boolean b = true;
	if(partie != null)
	    b = partie.fermer();
	return super.fermer() && cpt.fermer() && b;
    }

    @Override
    public boolean lancer() {
	infos.setEtat(InfoServeur.ETAT_ATTENTE);
	return super.lancer() && cpt.lancer();
    }

    @Override
    public boolean rejoindre(ClientServeur c) {
	if(super.rejoindre(c)) {
	    c.write(new Paquet(TypePaquet.TEMPS, new IO().addShort(cpt.getTemps())));
	    infos.setJoueurs(getClients().size());
	    return true;
	}
	return false;
    }

    @Override
    public void deconnexion(AbstractClient client) {
	super.deconnexion(client);
	infos.setJoueurs(getClients().size());
	if(ressources.aRessource(TypeRessource.PERSO, client.getID()))
	    ressources.removeRessource(TypeRessource.PERSO, client.getID());
    }

    @Override
    public Paquet getPaquetConnexion() {
	return new PaquetInfoServeur(getInfosServeur());
    }

    @Override
    public int getMaxClients() {
	return getInfosServeur().getMaxJoueurs();
    }

    @Override
    public RessourcesServeur getRessources() {
	return ressources;
    }

    @Override
    public ClientServeur creerClient(Socket socket) throws IOException {
	return new ClientServeur(this, socket);
    }

    @Override
    public void finChargement() {
	if(partie.estLancee())
	    partie.finPartie();
	else {
	    cpt.setTemps(getInfosServeur().getTemps());
	    partie.lancer();
	}
    }

    public static void main(String... args) throws IOException {
	Serveur s = new Serveur("test");
	s.configurer(map.Map.getAllMaps().get(0));
	s.lancer();
	IconeTaches.getInstance();
    }

}
